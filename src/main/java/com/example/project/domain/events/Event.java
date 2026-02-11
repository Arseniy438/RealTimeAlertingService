package com.example.project.domain.events;

import com.example.project.exceptions.NotSupportedTypeException;

import java.time.Instant;
import java.util.Map;
import java.util.Optional;

public class Event {

    private Long id;
    private final EventType type;
    private final Instant occurredAt;
    private final Map<EventField, Object> data;

    public Event(EventType type, Instant timestamp, Map<EventField, Object> data) {
        this.type = type;
        this.occurredAt = timestamp;
        validate(type, data);
        this.data = Map.copyOf(data);
    }

    public Event(Long id, EventType type, Instant timestamp, Map<EventField, Object> data) {
        this.id = id;
        this.type = type;
        this.occurredAt = timestamp;
        validate(type, data);
        this.data = Map.copyOf(data);
    }

    public Optional<Double> getDouble(EventField field) {
        Object value = data.get(field);
        if (value instanceof Number number) {
            return Optional.of(number.doubleValue());
        }
        return Optional.empty();
    }

    private void validate(EventType type, Map<EventField, Object> data) {
        for (EventField eventField : data.keySet()) {
            if (!type.supports(eventField)) {
                throw new NotSupportedTypeException(
                        "Field " + eventField + " not allowed for event type " + type
                );
            }
        }

    }

    public Long getId() {
        return id;
    }

    public EventType getType() {
        return type;
    }

    public Instant getOccurredAt() {
        return occurredAt;
    }

    public Map<EventField, Object> getData() {
        return data;
    }

    public void setId(long andIncrement) {
        id = andIncrement;
    }
}
