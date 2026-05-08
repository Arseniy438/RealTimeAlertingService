package com.example.project.dto.response;

import com.example.project.domain.events.EventType;

import java.time.Instant;
import java.util.Map;

/**
 * DTO for {@link com.example.project.domain.events.Event}
 */
public record EventResponse(Long id, EventType type, Instant occurredAt, Map<String, Object> data) {
}