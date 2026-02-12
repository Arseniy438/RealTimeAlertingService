package com.example.project.infrastructure.persistence.mapper;

import com.example.project.domain.events.Event;
import com.example.project.domain.events.EventField;
import com.example.project.exceptions.EventMappingException;
import com.example.project.infrastructure.persistence.EventEntity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class EventMapper {

    private final ObjectMapper objectMapper;

    public EventMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public EventEntity toEntity(Event domain) {
        if (domain == null) return null;

        String payloadJson = serializePayload(domain.getData());
        return new EventEntity(domain.getType(), domain.getOccurredAt(), payloadJson);
    }

    public Event toDomain(EventEntity entity) {
        if (entity == null) return null;

        Map<EventField, Object> data = deserializePayload(entity.getPayloadJson());

        return new Event(entity.getId(), entity.getType(), entity.getOccurredAt(), data);
    }

    private String serializePayload(Map<EventField, Object> data) {
        if (data == null || data.isEmpty()) {
            return "{}";
        }
        try {
            Map<String, Object> stringKeyMap = data.entrySet().stream()
                    .collect(Collectors.toMap(
                            e -> e.getKey().name(),
                            Map.Entry::getValue));

            return objectMapper.writeValueAsString(stringKeyMap);
        } catch (JsonProcessingException e) {
            throw new EventMappingException("Cannot serialize event payload", e);
        }
    }

    private Map<EventField, Object> deserializePayload(String payloadJson) {
        if (payloadJson == null || payloadJson.isEmpty()) {
            return new EnumMap<>(EventField.class);
        }

        try {
            Map<String, Object> stringKeyMap = objectMapper.readValue(payloadJson, new TypeReference<>() {});
            Map<EventField, Object> result = new EnumMap<>(EventField.class);

            for (Map.Entry<String, Object> entry : stringKeyMap.entrySet()) {
                EventField field;
                try {
                    field = EventField.valueOf(entry.getKey());
                } catch (IllegalArgumentException e) {
                    throw new EventMappingException("Unknown event field key: " + entry.getKey(), e);
                }
                result.put(field, entry.getValue());
            }
            return result;
        } catch (JsonProcessingException e) {
            throw new EventMappingException("Cannot deserialize event payload", e);
        }
    }
}
