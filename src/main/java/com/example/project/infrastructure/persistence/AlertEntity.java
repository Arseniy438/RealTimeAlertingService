package com.example.project.infrastructure.persistence;

import com.example.project.domain.alerts.AlertStatus;
import com.example.project.domain.events.Event;
import com.example.project.domain.rules.AlertRule;
import com.example.project.domain.rules.Severity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;

import java.time.Instant;

@Entity
@Table(name = "alerts")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class AlertEntity {

    @Id
    @Column(name = "alert_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rule_id")
    private AlertRule ruleId;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "alert_status")
    private AlertStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "severity")
    private Severity severity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")
    private Event event;

    @Column(name = "retry_count")
    private int retryCount;

    @Column(name = "last_triggered_at")
    private Instant lastTriggeredAt;


    public AlertEntity(AlertRule ruleId, Instant createdAt, Instant updatedAt, AlertStatus status, Severity severity, Event event, int retryCount, Instant lastTriggeredAt) {
        this.ruleId = ruleId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.status = status;
        this.severity = severity;
        this.event = event;
        this.retryCount = retryCount;
        this.lastTriggeredAt = lastTriggeredAt;
    }

    public Long getId() {
        return id;
    }

    public AlertRule getRuleId() {
        return ruleId;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public AlertStatus getStatus() {
        return status;
    }

    public Severity getSeverity() {
        return severity;
    }

    public Event getEvent() {
        return event;
    }

    public int getRetryCount() {
        return retryCount;
    }

    public Instant getLastTriggeredAt() {
        return lastTriggeredAt;
    }
}
