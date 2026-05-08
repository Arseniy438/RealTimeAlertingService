package com.example.project.domain.repository;

import com.example.project.domain.events.Event;
import com.example.project.domain.events.EventType;
import java.util.List;
import java.util.Optional;

public interface EventRepository{

    Event save(Event event);

    Optional<Event> findById(Long id);

    List<Event> findByType(EventType type);

    void deleteEvent(Long id);

    boolean existsById(Long id);
}
