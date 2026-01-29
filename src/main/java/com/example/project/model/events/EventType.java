package com.example.project.model.events;

import java.util.Set;

public enum EventType {

    CPU(Set.of(EventField.CPU_USAGE)),
    DISK(Set.of(EventField.DISK_FREE)),
    SERVICE(Set.of(EventField.RESPONSE_TIME));


    private final Set<EventField> eventFields;

    EventType(Set<EventField> eventFields){
        this.eventFields = eventFields;
    }

    public boolean supports(EventField field){
        return eventFields.contains(field);
    }

    public Set<EventField> getEventFields() {
        return eventFields;
    }
}
