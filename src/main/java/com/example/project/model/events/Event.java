package com.example.project.model.events;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

public class Event {

    private Long id;
    private final EventType type;
    private final LocalDateTime timestamp;
    private final Map<EventField, Object> data;

    public Event(EventType type, LocalDateTime timestamp, Map<EventField, Object> data) {
        this.type = type;
        this.timestamp = timestamp;
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
                throw new IllegalArgumentException(
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

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public Map<EventField, Object> getData() {
        return data;
    }

    public void setId(long andIncrement) {
        id = andIncrement;
    }
}
