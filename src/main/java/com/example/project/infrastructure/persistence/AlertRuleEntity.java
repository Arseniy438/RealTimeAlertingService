package com.example.project.infrastructure.persistence;

import com.example.project.domain.events.EventField;
import com.example.project.domain.events.EventType;
import com.example.project.domain.rules.conditions.Condition;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import com.example.project.domain.rules.Severity;
import java.time.Instant;

@Entity
@Table(name = "alert_rules")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class AlertRuleEntity {

    @Id
    @Column(name = "rule_id")
    private Long id;

    @Column(name = "rule_name")
    private String name;

    @Column(name = "enabled")
    private boolean isEnabled;

    @Enumerated(EnumType.STRING)
    @Column(name = "event_type")
    private EventType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "event_field")
    private EventField field;

    @Enumerated(EnumType.STRING)
    @Column(name = "severity")
    private Severity severity;

    @JoinColumn(name = "condition_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Condition condition;

    @Column(name = "created_at")
    private Instant createdAt;


    public AlertRuleEntity(String name, boolean isEnabled, EventType type, EventField field, Severity severity, Condition condition, Instant createdAt) {
        this.name = name;
        this.isEnabled = isEnabled;
        this.type = type;
        this.field = field;
        this.severity = severity;
        this.condition = condition;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public EventType getType() {
        return type;
    }

    public EventField getField() {
        return field;
    }

    public Severity getSeverity() {
        return severity;
    }

    public Condition getCondition() {
        return condition;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
