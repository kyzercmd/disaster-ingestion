package com.bd.disaster.service;

import com.bd.disaster.model.DisasterEvent;

public interface EventValidationService {
    /** Validates the event; throws InvalidEventException if invalid. */
    void validate(DisasterEvent event);
}
