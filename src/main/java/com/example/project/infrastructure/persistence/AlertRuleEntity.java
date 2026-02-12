package com.example.project.infrastructure.persistence;

import com.example.project.domain.events.EventField;
import com.example.project.domain.events.EventType;
import com.example.project.domain.rules.Severity;
import com.example.project.domain.rules.conditions.ConditionType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "alert_rules")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class AlertRuleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rule_id")
    private Long id;

    @Column(name = "rule_name", nullable = false)
    private String name;

    @Column(name = "enabled", nullable = false)
    private boolean enabled;

    @Enumerated(EnumType.STRING)
    @Column(name = "event_type", nullable = false)
    private EventType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "event_field", nullable = false)
    private EventField field;

    @Enumerated(EnumType.STRING)
    @Column(name = "severity", nullable = false)
    private Severity severity;

    @Enumerated(EnumType.STRING)
    @Column(name = "condition_type", nullable = false)
    private ConditionType conditionType;

    @Column(name = "condition_threshold", nullable = false)
    private double conditionThreshold;

    @Column(name = "description")
    private String description;

    @Column(name = "cooldown_in_seconds", nullable = false)
    private int cooldownInSeconds;

    @Column(name = "max_retries", nullable = false)
    private int maxRetries;

    @Column(name = "comparison_window_seconds", nullable = false)
    private long comparisonWindowSeconds;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    public AlertRuleEntity(
            String name,
            boolean enabled,
            EventType type,
            EventField field,
            Severity severity,
            ConditionType conditionType,
            double conditionThreshold,
            String description,
            int cooldownInSeconds,
            int maxRetries,
            long comparisonWindowSeconds,
            Instant createdAt
    ) {
        this.name = name;
        this.enabled = enabled;
        this.type = type;
        this.field = field;
        this.severity = severity;
        this.conditionType = conditionType;
        this.conditionThreshold = conditionThreshold;
        this.description = description;
        this.cooldownInSeconds = cooldownInSeconds;
        this.maxRetries = maxRetries;
        this.comparisonWindowSeconds = comparisonWindowSeconds;
        this.createdAt = createdAt;
    }
}
