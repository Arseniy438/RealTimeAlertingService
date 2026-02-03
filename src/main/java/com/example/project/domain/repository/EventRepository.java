package com.example.project.domain.repository;

import com.example.project.domain.events.Event;

import java.util.Optional;

public interface EventRepository {
    void addEvent(Event event);

    Optional<Event> getEvent(Long id);

    void deleteEvent(Long id);
}
