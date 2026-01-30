package com.example.project.repository;

import com.example.project.model.events.Event;

import java.util.Optional;

public interface IEventRepository {
    void addEvent(Event event);

    Optional<Event> getEvent(Long id);

    void deleteEvent(Long id);
}
