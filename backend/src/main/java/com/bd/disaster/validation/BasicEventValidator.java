package com.bd.disaster.validation;

import com.bd.disaster.model.DisasterEvent;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class BasicEventValidator implements EventValidator {

    private static final Set<String> VALID_HAZARD_TYPES =
        Set.of("flood", "cyclone", "drought", "river_erosion");

    private static final Set<String> VALID_DIVISIONS =
        Set.of("Barishal", "Chattogram", "Dhaka", "Khulna",
               "Mymensingh", "Rajshahi", "Rangpur", "Sylhet");

    @Override
    public ValidationResult validate(DisasterEvent e) {
        List<String> errors = new ArrayList<>();

        if (isBlank(e.getEventId()))
            errors.add("event_id is required");

        if (isBlank(e.getHazardType()) || !VALID_HAZARD_TYPES.contains(e.getHazardType()))
            errors.add("hazard_type must be one of: flood, cyclone, drought, river_erosion");

        if (isBlank(e.getDivision()) || !VALID_DIVISIONS.contains(e.getDivision()))
            errors.add("division must be one of the 8 valid Bangladesh divisions");

        if (isBlank(e.getDistrict()))
            errors.add("district is required");

        if (isBlank(e.getUpazila()))
            errors.add("upazila is required");

        if (isBlank(e.getSourceUrl()))
            errors.add("source_url is required");

        if (e.getAffectedPeople() == null || e.getAffectedPeople() < 0)
            errors.add("affected_people must be an integer >= 0");

        if (e.getDeaths() == null || e.getDeaths() < 0)
            errors.add("deaths must be an integer >= 0");

        if (e.getEconomicLossBdt() == null || e.getEconomicLossBdt() < 0)
            errors.add("economic_loss_bdt must be a number >= 0");

        LocalDate start = null, end = null;
        try {
            if (!isBlank(e.getStartDate())) start = LocalDate.parse(e.getStartDate());
            else errors.add("start_date is required (YYYY-MM-DD)");
        } catch (DateTimeParseException ex) {
            errors.add("start_date must be in YYYY-MM-DD format");
        }
        try {
            if (!isBlank(e.getEndDate())) end = LocalDate.parse(e.getEndDate());
            else errors.add("end_date is required (YYYY-MM-DD)");
        } catch (DateTimeParseException ex) {
            errors.add("end_date must be in YYYY-MM-DD format");
        }
        if (start != null && end != null && end.isBefore(start))
            errors.add("end_date must be >= start_date");

        return errors.isEmpty() ? ValidationResult.ok() : ValidationResult.fail(errors);
    }

    protected boolean isBlank(String s) {
        return s == null || s.isBlank();
    }
}
