package com.bd.disaster.controller;

import com.bd.disaster.exception.InvalidEventException;
import com.bd.disaster.model.DisasterEvent;
import com.bd.disaster.repository.EventRepository;
import com.bd.disaster.service.CsvUploadService;
import com.bd.disaster.service.EventValidationService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/api/events")
public class EventController {

    private final EventValidationService validationService;
    private final EventRepository repository;
    private final CsvUploadService csvUploadService;

    @Value("${disaster.csv.path:data/disaster_events.csv}")
    private String csvPath;

    public EventController(EventValidationService validationService,
                           EventRepository repository,
                           CsvUploadService csvUploadService) {
        this.validationService = validationService;
        this.repository        = repository;
        this.csvUploadService  = csvUploadService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DisasterEvent> addEvent(@RequestBody DisasterEvent event) {
        if (repository.existsById(event.getEventId()))
            throw new InvalidEventException("Duplicate event_id: " + event.getEventId());
        validationService.validate(event);
        computeDerived(event);
        repository.save(event);
        return ResponseEntity.status(HttpStatus.CREATED).body(event);
    }

    @PostMapping("/upload")
    public ResponseEntity<CsvUploadService.UploadResult> uploadCsv(
            @RequestParam("file") MultipartFile file) throws IOException {
        CsvUploadService.UploadResult result = csvUploadService.processUpload(file.getInputStream());
        return ResponseEntity.ok(result);
    }

    @GetMapping(produces = "text/csv")
    public ResponseEntity<byte[]> downloadCsv() throws IOException {
        byte[] content = Files.readAllBytes(Path.of(csvPath));
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"disaster_events.csv\"")
            .contentType(MediaType.parseMediaType("text/csv"))
            .body(content);
    }

    @GetMapping("/summary")
    public ResponseEntity<Map<String, Object>> getSummary() {
        return ResponseEntity.ok(repository.getSummary());
    }

    private void computeDerived(DisasterEvent ev) {
        if (ev.getStartDate() != null && ev.getEndDate() != null && ev.getDurationDays() == null) {
            LocalDate s = LocalDate.parse(ev.getStartDate());
            LocalDate e = LocalDate.parse(ev.getEndDate());
            ev.setDurationDays((int)(e.toEpochDay() - s.toEpochDay()) + 1);
        }
        if (ev.getEconomicLossUsd() == null && ev.getEconomicLossBdt() != null)
            ev.setEconomicLossUsd(ev.getEconomicLossBdt() / 110.0);
    }
}
