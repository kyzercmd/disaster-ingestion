package com.bd.disaster.validation;

import com.bd.disaster.model.DisasterEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class StrictEventValidatorTest {

    private StrictEventValidator validator;

    private DisasterEvent validEvent() {
        DisasterEvent e = new DisasterEvent();
        e.setEventId("BD-CYC-2024-001");
        e.setHazardType("cyclone");
        e.setStartDate("2024-05-20");
        e.setEndDate("2024-05-22");
        e.setDivision("Barishal");
        e.setDistrict("Barguna");
        e.setUpazila("Amtali");
        e.setAffectedPeople(10000L);
        e.setDeaths(15L);
        e.setEconomicLossBdt(2000000.0);
        e.setSourceUrl("https://example.com");
        return e;
    }

    @BeforeEach
    void setUp() { validator = new StrictEventValidator(); }

    @Test
    void testValidEventPasses() {
        assertTrue(validator.validate(validEvent()).isValid());
    }

    @Test
    void testNonHttpSourceUrlFails() {
        DisasterEvent e = validEvent();
        e.setSourceUrl("ftp://example.com");
        ValidationResult r = validator.validate(e);
        assertFalse(r.isValid());
        assertTrue(r.getErrors().stream().anyMatch(s -> s.contains("source_url")));
    }

    @Test
    void testInvalidSeverityCategoryFails() {
        DisasterEvent e = validEvent();
        e.setSeverityCategory("critical");
        ValidationResult r = validator.validate(e);
        assertFalse(r.isValid());
        assertTrue(r.getErrors().stream().anyMatch(s -> s.contains("severity_category")));
    }

    @Test
    void testValidSeverityCategoryPasses() {
        DisasterEvent e = validEvent();
        e.setSeverityCategory("high");
        assertTrue(validator.validate(e).isValid());
    }
}
