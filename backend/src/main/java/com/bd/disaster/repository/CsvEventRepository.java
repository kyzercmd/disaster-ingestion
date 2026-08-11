package com.bd.disaster.repository;

import com.bd.disaster.exception.StorageException;
import com.bd.disaster.model.DisasterEvent;
import org.apache.commons.csv.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.nio.file.*;
import java.util.*;

@Repository
public class CsvEventRepository implements EventRepository {

    private static final String[] HEADERS = {
        "event_id","hazard_type","start_date","end_date","division","district","upazila",
        "affected_people","deaths","economic_loss_bdt","economic_loss_usd","duration_days",
        "severity_category","source_url","notes"
    };

    private final String csvPath;

    public CsvEventRepository(@Value("${disaster.csv.path:data/disaster_events.csv}") String csvPath) {
        this.csvPath = csvPath;
        ensureHeaderExists();
    }

    private void ensureHeaderExists() {
        File f = new File(csvPath);
        if (f.getParentFile() != null) f.getParentFile().mkdirs();
        if (!f.exists() || f.length() == 0) {
            try (PrintWriter pw = new PrintWriter(new FileWriter(f))) {
                pw.println(String.join(",", HEADERS));
            } catch (IOException e) {
                throw new StorageException("Cannot initialize CSV file: " + csvPath, e);
            }
        }
    }

    @Override
    public synchronized void save(DisasterEvent ev) {
        try (FileWriter fw = new FileWriter(csvPath, true);
             CSVPrinter printer = new CSVPrinter(fw, CSVFormat.DEFAULT.withRecordSeparator("\n"))) {
            printer.printRecord(
                ev.getEventId(), ev.getHazardType(), ev.getStartDate(), ev.getEndDate(),
                ev.getDivision(), ev.getDistrict(), ev.getUpazila(),
                ev.getAffectedPeople(), ev.getDeaths(), ev.getEconomicLossBdt(),
                ev.getEconomicLossUsd(), ev.getDurationDays(), ev.getSeverityCategory(),
                ev.getSourceUrl(), ev.getNotes()
            );
        } catch (IOException e) {
            throw new StorageException("Failed to save event: " + ev.getEventId(), e);
        }
    }

    @Override
    public List<DisasterEvent> findAll() {
        List<DisasterEvent> events = new ArrayList<>();
        try (Reader reader = new FileReader(csvPath);
             CSVParser parser = CSVFormat.DEFAULT.withFirstRecordAsHeader()
                                                 .withIgnoreEmptyLines()
                                                 .parse(reader)) {
            for (CSVRecord rec : parser) {
                events.add(recordToEvent(rec));
            }
        } catch (IOException e) {
            throw new StorageException("Failed to read events CSV", e);
        }
        return events;
    }

    @Override
    public boolean existsById(String eventId) {
        return findAll().stream().anyMatch(e -> eventId.equals(e.getEventId()));
    }

    @Override
    public Map<String, Object> getSummary() {
        List<DisasterEvent> all = findAll();
        Map<String, Long> byHazard = new LinkedHashMap<>();
        Map<String, Long> byDivision = new LinkedHashMap<>();
        Map<String, Long> byYear = new LinkedHashMap<>();

        for (DisasterEvent e : all) {
            byHazard.merge(e.getHazardType(), 1L, Long::sum);
            byDivision.merge(e.getDivision(), 1L, Long::sum);
            if (e.getStartDate() != null && e.getStartDate().length() >= 4)
                byYear.merge(e.getStartDate().substring(0, 4), 1L, Long::sum);
        }

        Map<String, Object> summary = new LinkedHashMap<>();
        summary.put("total", (long) all.size());
        summary.put("byHazardType", byHazard);
        summary.put("byDivision", byDivision);
        summary.put("byYear", byYear);
        return summary;
    }

    private DisasterEvent recordToEvent(CSVRecord rec) {
        DisasterEvent e = new DisasterEvent();
        e.setEventId(rec.get("event_id"));
        e.setHazardType(rec.get("hazard_type"));
        e.setStartDate(rec.get("start_date"));
        e.setEndDate(rec.get("end_date"));
        e.setDivision(rec.get("division"));
        e.setDistrict(rec.get("district"));
        e.setUpazila(rec.get("upazila"));
        safeSetLong(e::setAffectedPeople, rec.get("affected_people"));
        safeSetLong(e::setDeaths, rec.get("deaths"));
        safeSetDouble(e::setEconomicLossBdt, rec.get("economic_loss_bdt"));
        safeSetDouble(e::setEconomicLossUsd, rec.get("economic_loss_usd"));
        safeSetInt(e::setDurationDays, rec.get("duration_days"));
        e.setSeverityCategory(nullIfBlank(rec.get("severity_category")));
        e.setSourceUrl(rec.get("source_url"));
        e.setNotes(nullIfBlank(rec.get("notes")));
        return e;
    }

    private void safeSetLong(java.util.function.Consumer<Long> setter, String val) {
        try { if (val != null && !val.isBlank()) setter.accept(Long.parseLong(val.trim())); }
        catch (NumberFormatException ignored) {}
    }

    private void safeSetDouble(java.util.function.Consumer<Double> setter, String val) {
        try { if (val != null && !val.isBlank()) setter.accept(Double.parseDouble(val.trim())); }
        catch (NumberFormatException ignored) {}
    }

    private void safeSetInt(java.util.function.Consumer<Integer> setter, String val) {
        try { if (val != null && !val.isBlank()) setter.accept(Integer.parseInt(val.trim())); }
        catch (NumberFormatException ignored) {}
    }

    private String nullIfBlank(String s) { return (s == null || s.isBlank()) ? null : s; }
}
