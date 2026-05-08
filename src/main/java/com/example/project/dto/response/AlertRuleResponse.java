package com.example.project.dto.response;

import com.example.project.domain.events.EventField;
import com.example.project.domain.events.EventType;
import com.example.project.domain.rules.Severity;

import java.time.Duration;
import java.time.Instant;

/**
 * DTO for {@link com.example.project.domain.rules.AlertRule}
 */
public record AlertRuleResponse(Long id, String name, boolean enabled, EventType eventType, EventField eventField,
                                Severity severity, ConditionResponse condition, Instant createdAt, String description,
                                int cooldownInSeconds, int maxRetries, Duration comparisonWindow) {
}