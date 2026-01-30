package com.example.project.repository;

import com.example.project.model.events.Event;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public class EventRepository implements IEventRepository {

    private final Map<Long, Event> eventMap = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public void addEvent(Event event) {
        if (event.getId() == null) {
            event.setId(idGenerator.getAndIncrement());
        }
        eventMap.put(event.getId(), event);
    }

    @Override
    public Optional<Event> getEvent(Long id) {
        return Optional.ofNullable(eventMap.get(id));
    }

    @Override
    public void deleteEvent(Long id) {
        if (eventMap.containsKey(id)) {
            eventMap.remove(id);
        }
    }
}
