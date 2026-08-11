package com.bd.disaster.validation;

import com.bd.disaster.model.DisasterEvent;

public interface EventValidator {
    ValidationResult validate(DisasterEvent event);
}
