package com.example.project.dto.request;

import com.example.project.domain.events.EventField;
import com.example.project.domain.events.EventType;
import com.example.project.domain.rules.AlertRule;
import com.example.project.domain.rules.Severity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Value;

/**
 * DTO for {@link AlertRule}
 */
@Value
public class AlertRuleRequest {
    @NotEmpty
    String name;
    boolean enabled;
    @NotNull
    EventType eventType;
    @NotNull
    EventField eventField;
    @NotNull
    Severity severity;
    ConditionRequest condition;
    @NotBlank
    String description;
    @Positive
    int cooldownInSeconds;
    @Positive
    int maxRetries;
    @NotNull
    Long comparisonWindow;
}