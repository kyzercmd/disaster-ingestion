package com.bd.disaster.controller;

import com.bd.disaster.model.DisasterEvent;
import com.bd.disaster.repository.EventRepository;
import com.bd.disaster.service.CsvUploadService;
import com.bd.disaster.service.EventValidationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EventController.class)
class EventControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;

    @MockBean EventValidationService validationService;
    @MockBean EventRepository repository;
    @MockBean CsvUploadService csvUploadService;

    private DisasterEvent validEvent() {
        DisasterEvent e = new DisasterEvent();
        e.setEventId("BD-FLOOD-2024-001");
        e.setHazardType("flood");
        e.setStartDate("2024-06-01");
        e.setEndDate("2024-06-15");
        e.setDivision("Dhaka");
        e.setDistrict("Manikganj");
        e.setUpazila("Singair");
        e.setAffectedPeople(5000L);
        e.setDeaths(3L);
        e.setEconomicLossBdt(1000000.0);
        e.setSourceUrl("https://example.com");
        return e;
    }

    @Test
    void testAddEventReturns201() throws Exception {
        when(repository.existsById("BD-FLOOD-2024-001")).thenReturn(false);
        doNothing().when(validationService).validate(any());
        doNothing().when(repository).save(any());

        mockMvc.perform(post("/api/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validEvent())))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.eventId").value("BD-FLOOD-2024-001"));
    }

    @Test
    void testSummaryEndpoint() throws Exception {
        when(repository.getSummary()).thenReturn(Map.of("total", 5L));
        mockMvc.perform(get("/api/events/summary"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.total").value(5));
    }
}
