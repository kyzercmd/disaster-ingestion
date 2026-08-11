package com.bd.disaster.repository;

import com.bd.disaster.model.DisasterEvent;
import java.util.List;
import java.util.Map;

public interface EventRepository {
    void save(DisasterEvent event);
    List<DisasterEvent> findAll();
    boolean existsById(String eventId);
    Map<String, Object> getSummary();
}
