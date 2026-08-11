package com.bd.disaster.validation;

import com.bd.disaster.model.DisasterEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BasicEventValidatorTest {

    private BasicEventValidator validator;

    private DisasterEvent validEvent() {
        DisasterEvent e = new DisasterEvent();
        e.setEventId("BD-FLOOD-2024-001");
        e.setHazardType("flood");
        e.setStartDate("2024-06-01");
        e.setEndDate("2024-06-15");
        e.setDivision("Dhaka");
        e.setDistrict("Manikganj");
        e.setUpazila("Singair");
        e.setAffectedPeople(500L);
        e.setDeaths(2L);
        e.setEconomicLossBdt(500000.0);
        e.setSourceUrl("https://example.com");
        return e;
    }

    @BeforeEach
    void setUp() { validator = new BasicEventValidator(); }

    @Test
    void testValidEvent() {
        assertTrue(validator.validate(validEvent()).isValid());
    }

    @Test
    void testMissingEventId() {
        DisasterEvent e = validEvent();
        e.setEventId(null);
        ValidationResult r = validator.validate(e);
        assertFalse(r.isValid());
        assertTrue(r.getErrors().stream().anyMatch(s -> s.contains("event_id")));
    }

    @Test
    void testInvalidHazardType() {
        DisasterEvent e = validEvent();
        e.setHazardType("earthquake");
        ValidationResult r = validator.validate(e);
        assertFalse(r.isValid());
        assertTrue(r.getErrors().stream().anyMatch(s -> s.contains("hazard_type")));
    }

    @Test
    void testInvalidDivision() {
        DisasterEvent e = validEvent();
        e.setDivision("Comilla");
        ValidationResult r = validator.validate(e);
        assertFalse(r.isValid());
        assertTrue(r.getErrors().stream().anyMatch(s -> s.contains("division")));
    }

    @Test
    void testNegativeDeaths() {
        DisasterEvent e = validEvent();
        e.setDeaths(-1L);
        ValidationResult r = validator.validate(e);
        assertFalse(r.isValid());
        assertTrue(r.getErrors().stream().anyMatch(s -> s.contains("deaths")));
    }

    @Test
    void testEndDateBeforeStartDate() {
        DisasterEvent e = validEvent();
        e.setStartDate("2024-06-15");
        e.setEndDate("2024-06-01");
        ValidationResult r = validator.validate(e);
        assertFalse(r.isValid());
        assertTrue(r.getErrors().stream().anyMatch(s -> s.contains("end_date")));
    }
}
