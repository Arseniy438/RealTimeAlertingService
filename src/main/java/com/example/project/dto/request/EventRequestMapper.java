package com.example.project.dto.request;

import com.example.project.domain.events.Event;
import com.example.project.domain.events.EventField;
import com.example.project.exceptions.EventMappingException;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface EventRequestMapper {

    default Event toDomain(EventRequest eventRequest){
        Map<EventField, Object> values = new HashMap<>();
        if (eventRequest.getData() != null) {
            for (Map.Entry<String, Object> entry : eventRequest.getData().entrySet()) {
                EventField key;
                try{key = EventField.valueOf(entry.getKey());} // строка → enum
                catch (IllegalArgumentException e){
                    throw new EventMappingException("Unknown event field key: " + entry.getKey(), e);
                }
                values.put(key, entry.getValue());
            }
        }

        return new Event(eventRequest.getType(), Instant.now(), values);
    }
}