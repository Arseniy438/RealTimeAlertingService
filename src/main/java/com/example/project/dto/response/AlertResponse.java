package com.example.project.dto.response;

import com.example.project.domain.alerts.AlertStatus;
import com.example.project.domain.rules.Severity;

import java.time.Instant;

/**
 * DTO for {@link com.example.project.domain.alerts.Alert}
 */
public record AlertResponse(Long id, AlertRuleResponse rule, String message, Instant createdAt, Instant updatedAt,
                            AlertStatus status, Severity severity, EventResponse event, int retryCount,
                            Instant lastTriggeredAt) {
}