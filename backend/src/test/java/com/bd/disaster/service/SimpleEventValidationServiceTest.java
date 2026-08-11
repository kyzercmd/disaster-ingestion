package com.bd.disaster.service;

import com.bd.disaster.exception.InvalidEventException;
import com.bd.disaster.model.DisasterEvent;
import com.bd.disaster.validation.StrictEventValidator;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SimpleEventValidationServiceTest {

    private final SimpleEventValidationService svc =
        new SimpleEventValidationService(new StrictEventValidator());

    private DisasterEvent validEvent() {
        DisasterEvent e = new DisasterEvent();
        e.setEventId("BD-DRG-2024-001");
        e.setHazardType("drought");
        e.setStartDate("2024-03-01");
        e.setEndDate("2024-05-31");
        e.setDivision("Rajshahi");
        e.setDistrict("Rajshahi");
        e.setUpazila("Paba");
        e.setAffectedPeople(80000L);
        e.setDeaths(0L);
        e.setEconomicLossBdt(5000000.0);
        e.setSourceUrl("https://example.com");
        return e;
    }

    @Test
    void testValidEventDoesNotThrow() {
        assertDoesNotThrow(() -> svc.validate(validEvent()));
    }

    @Test
    void testInvalidEventThrows() {
        DisasterEvent e = validEvent();
        e.setHazardType("tornado");
        assertThrows(InvalidEventException.class, () -> svc.validate(e));
    }
}
