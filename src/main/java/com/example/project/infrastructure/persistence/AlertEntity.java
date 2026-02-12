package com.example.project.infrastructure.persistence;

import com.example.project.domain.alerts.AlertStatus;
import com.example.project.domain.rules.Severity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "alerts")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class AlertEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "alert_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "rule_id", nullable = false)
    private AlertRuleEntity rule;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "event_id", nullable = false)
    private EventEntity event;

    @Column(name = "message", nullable = false)
    private String message;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "alert_status", nullable = false)
    private AlertStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "severity", nullable = false)
    private Severity severity;

    @Column(name = "retry_count", nullable = false)
    private int retryCount;

    @Column(name = "last_triggered_at")
    private Instant lastTriggeredAt;


    public void setId(Long id) {
        this.id = id;
    }

    public AlertEntity(
            AlertRuleEntity rule,
            EventEntity event,
            String message,
            Instant createdAt,
            Instant updatedAt,
            AlertStatus status,
            Severity severity,
            int retryCount,
            Instant lastTriggeredAt
    ) {
        this.rule = rule;
        this.event = event;
        this.message = message;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.status = status;
        this.severity = severity;
        this.retryCount = retryCount;
        this.lastTriggeredAt = lastTriggeredAt;
    }
}
