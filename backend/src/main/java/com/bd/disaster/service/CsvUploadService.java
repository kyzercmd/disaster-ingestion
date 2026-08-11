package com.bd.disaster.service;

import com.bd.disaster.exception.CsvParseException;
import com.bd.disaster.model.DisasterEvent;
import com.bd.disaster.repository.EventRepository;
import com.bd.disaster.validation.EventValidator;
import com.bd.disaster.validation.ValidationResult;
import org.apache.commons.csv.*;
import org.springframework.stereotype.Service;

import java.io.*;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.*;

@Service
public class CsvUploadService {

    private final EventRepository repository;
    private final EventValidator validator;
    private final ExecutorService executor;

    public record UploadResult(int accepted, int rejected, List<String> errors) {}

    public CsvUploadService(EventRepository repository,
                            EventValidator validator,
                            ExecutorService executor) {
        this.repository = repository;
        this.validator  = validator;
        this.executor   = executor;
    }

    public UploadResult processUpload(InputStream csv) {
        List<CSVRecord> records;
        try (Reader reader = new InputStreamReader(csv);
             CSVParser parser = CSVFormat.DEFAULT.withFirstRecordAsHeader()
                                                 .withIgnoreEmptyLines()
                                                 .parse(reader)) {
            records = parser.getRecords();
        } catch (IOException e) {
            throw new CsvParseException("Failed to parse uploaded CSV", e);
        }

        List<Callable<RowResult>> tasks = records.stream()
            .map(rec -> (Callable<RowResult>) () -> validateRow(rec))
            .toList();

        List<Future<RowResult>> futures;
        try {
            futures = executor.invokeAll(tasks);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new CsvParseException("Upload interrupted", e);
        }

        int accepted = 0, rejected = 0;
        List<String> errors = new ArrayList<>();

        for (int i = 0; i < futures.size(); i++) {
            try {
                RowResult rr = futures.get(i).get();
                if (rr.valid()) {
                    repository.save(rr.event());
                    accepted++;
                } else {
                    rejected++;
                    errors.add("Row " + (i + 2) + ": " + rr.error());
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                rejected++;
                errors.add("Row " + (i + 2) + ": interrupted");
            } catch (ExecutionException e) {
                rejected++;
                errors.add("Row " + (i + 2) + ": " + e.getCause().getMessage());
            }
        }

        return new UploadResult(accepted, rejected, errors);
    }

    private RowResult validateRow(CSVRecord rec) {
        try {
            DisasterEvent ev = recordToEvent(rec);

            if (repository.existsById(ev.getEventId()))
                return RowResult.fail("Duplicate event_id: " + ev.getEventId());

            ValidationResult vr = validator.validate(ev);
            if (!vr.isValid())
                return RowResult.fail(String.join("; ", vr.getErrors()));

            computeDerived(ev);
            return RowResult.ok(ev);
        } catch (Exception e) {
            return RowResult.fail(e.getMessage());
        }
    }

    private void computeDerived(DisasterEvent ev) {
        if (ev.getStartDate() != null && ev.getEndDate() != null) {
            LocalDate s = LocalDate.parse(ev.getStartDate());
            LocalDate e = LocalDate.parse(ev.getEndDate());
            ev.setDurationDays((int)(e.toEpochDay() - s.toEpochDay()) + 1);
        }
        if (ev.getEconomicLossUsd() == null && ev.getEconomicLossBdt() != null)
            ev.setEconomicLossUsd(ev.getEconomicLossBdt() / 110.0);
    }

    private DisasterEvent recordToEvent(CSVRecord rec) {
        DisasterEvent e = new DisasterEvent();
        e.setEventId(get(rec, "event_id"));
        e.setHazardType(get(rec, "hazard_type"));
        e.setStartDate(get(rec, "start_date"));
        e.setEndDate(get(rec, "end_date"));
        e.setDivision(get(rec, "division"));
        e.setDistrict(get(rec, "district"));
        e.setUpazila(get(rec, "upazila"));
        safeSetLong(e::setAffectedPeople, get(rec, "affected_people"));
        safeSetLong(e::setDeaths, get(rec, "deaths"));
        safeSetDouble(e::setEconomicLossBdt, get(rec, "economic_loss_bdt"));
        safeSetDouble(e::setEconomicLossUsd, get(rec, "economic_loss_usd"));
        safeSetInt(e::setDurationDays, get(rec, "duration_days"));
        e.setSeverityCategory(get(rec, "severity_category"));
        e.setSourceUrl(get(rec, "source_url"));
        e.setNotes(get(rec, "notes"));
        return e;
    }

    private String get(CSVRecord rec, String col) {
        try { String v = rec.get(col); return (v == null || v.isBlank()) ? null : v.trim(); }
        catch (IllegalArgumentException ex) { return null; }
    }

    private void safeSetLong(java.util.function.Consumer<Long> s, String v) {
        try { if (v != null) s.accept(Long.parseLong(v)); } catch (NumberFormatException ignored) {}
    }
    private void safeSetDouble(java.util.function.Consumer<Double> s, String v) {
        try { if (v != null) s.accept(Double.parseDouble(v)); } catch (NumberFormatException ignored) {}
    }
    private void safeSetInt(java.util.function.Consumer<Integer> s, String v) {
        try { if (v != null) s.accept(Integer.parseInt(v)); } catch (NumberFormatException ignored) {}
    }

    private record RowResult(boolean valid, DisasterEvent event, String error) {
        static RowResult ok(DisasterEvent ev) { return new RowResult(true, ev, null); }
        static RowResult fail(String err) { return new RowResult(false, null, err); }
    }
}
