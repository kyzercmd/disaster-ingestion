package com.bd.disaster.service;

import com.bd.disaster.exception.InvalidEventException;
import com.bd.disaster.model.DisasterEvent;
import com.bd.disaster.validation.EventValidator;
import com.bd.disaster.validation.ValidationResult;
import org.springframework.stereotype.Service;

@Service
public class SimpleEventValidationService implements EventValidationService {

    private final EventValidator validator;

    public SimpleEventValidationService(EventValidator validator) {
        this.validator = validator;
    }

    @Override
    public void validate(DisasterEvent event) {
        ValidationResult result = validator.validate(event);
        if (!result.isValid()) {
            throw new InvalidEventException("Validation failed: " +
                String.join("; ", result.getErrors()));
        }
    }
}
