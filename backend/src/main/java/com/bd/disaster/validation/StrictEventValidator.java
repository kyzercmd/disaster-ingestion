package com.bd.disaster.validation;

import com.bd.disaster.model.DisasterEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class StrictEventValidator extends BasicEventValidator {

    private static final Set<String> VALID_SEVERITY =
        Set.of("low", "medium", "high", "extreme");

    @Override
    public ValidationResult validate(DisasterEvent e) {
        ValidationResult base = super.validate(e);
        List<String> errors = new ArrayList<>(base.getErrors());

        if (!isBlank(e.getSourceUrl()) && !e.getSourceUrl().startsWith("http"))
            errors.add("source_url must start with http or https");

        if (!isBlank(e.getSeverityCategory()) &&
            !VALID_SEVERITY.contains(e.getSeverityCategory()))
            errors.add("severity_category must be one of: low, medium, high, extreme");

        return errors.isEmpty() ? ValidationResult.ok() : ValidationResult.fail(errors);
    }
}
