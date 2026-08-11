package com.bd.disaster.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DisasterEventTest {

    @Test
    void testGettersAndSetters() {
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
        e.setSourceUrl("https://example.com/report");

        assertEquals("BD-FLOOD-2024-001", e.getEventId());
        assertEquals("flood", e.getHazardType());
        assertEquals("Dhaka", e.getDivision());
        assertEquals(5000L, e.getAffectedPeople());
        assertEquals(1000000.0, e.getEconomicLossBdt());
    }

    @Test
    void testDefaultOptionalFields() {
        DisasterEvent e = new DisasterEvent();
        assertNull(e.getSeverityCategory());
        assertNull(e.getNotes());
        assertNull(e.getEconomicLossUsd());
        assertNull(e.getDurationDays());
    }
}
