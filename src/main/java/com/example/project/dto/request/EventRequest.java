package com.example.project.dto.request;

import com.example.project.domain.events.EventType;
import jakarta.validation.constraints.NotNull;
import lombok.Value;

import java.time.Instant;
import java.util.Map;

/**
 * DTO for {@link com.example.project.domain.events.Event}
 */
@Value
public class EventRequest {
    @NotNull
    EventType type;
    Map<String, Object> data;
}