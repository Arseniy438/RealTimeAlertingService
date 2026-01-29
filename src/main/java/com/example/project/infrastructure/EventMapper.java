package com.example.project.infrastructure;

import com.example.project.model.events.Event;
import com.example.project.model.events.EventField;
import com.example.project.model.events.EventType;

import java.time.LocalDateTime;
import java.util.Map;

public class EventMapper {

    public Event toEvent(RawData raw) {
        EventType type = resolveType(raw.getType());
        LocalDateTime time = raw.getReceivedAt();

        Map<EventField, Object> data = Map.of(
                EventField.DISK_FREE, raw.get("value"),
                EventField.CPU_USAGE, raw.get("host"));

        return new Event(type, time, data);
    }


    private EventType resolveType(String type){
        EventType eventType = EventType.valueOf(type);
        return eventType;
    }


}
