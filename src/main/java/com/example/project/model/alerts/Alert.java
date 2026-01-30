package com.example.project.model.alerts;

import com.example.project.model.rules.AlertRule;
import com.example.project.model.events.Event;
import com.example.project.model.rules.Severity;
import java.time.LocalDateTime;


public class Alert {

    private Long id;
    private final AlertRule rule;
    private String message;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private AlertStatus status;
    private Severity severity;
    private final Event event;
    private int retryCount; // сколько раз ретраили


    public Alert(AlertRule rule, Event event, String message, Severity severity, int retryCount, LocalDateTime now) {
        this.rule = rule;
        this.message = message;
        this.severity = severity;
        this.event = event;
        this.retryCount = retryCount;
        this.status = AlertStatus.NEW;
        this.createdAt = now;
        this.updatedAt = now;
    }

    public void activate(LocalDateTime now) {
        if (status != AlertStatus.NEW) {
            throw new IllegalStateException("Cannot activate alert from " + status);
        }
        status = AlertStatus.ACTIVE;
        touch(now);
    }

    public static Alert create(AlertRule rule, Event event, LocalDateTime now) {
        Alert alert = new Alert(rule, event, rule.getDescription(), rule.getSeverity(), 0, now);
        alert.status = AlertStatus.ACTIVE;
        return alert;
    }

    public void failed(LocalDateTime now) {
        if (status != AlertStatus.FAILED) {
            status = AlertStatus.FAILED;
            touch(now);
        }
    }

    public void acknowledged(LocalDateTime now) {
        if (status != AlertStatus.ACTIVE) {
            throw new IllegalStateException("Cannot acknowledge alert from " + status);
        }
        status = AlertStatus.ACKNOWLEDGED;
        touch(now);
    }

    public void resolve(LocalDateTime now) {
        if (status != AlertStatus.ACTIVE && status != AlertStatus.ACKNOWLEDGED) {
            throw new IllegalStateException("Cannot resolve alert from " + status);
        }
        status = AlertStatus.RESOLVED;
        touch(now);
    }

    public void retry(LocalDateTime now) {
        retryCount++;
        if (retryCount >= rule.getMaxRetries()) {
            failed(now);

        }
        touch(now);
    }

    private void touch(LocalDateTime now) {
        updatedAt = now;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public Event getEvent() {
        return event;
    }

    public int getRetryCount() {
        return retryCount;
    }

    public Long getId() {
        return id;
    }

    public AlertRule getRule() {
        return rule;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public AlertStatus getStatus() {
        return status;
    }

    public Severity getSeverity() {
        return severity;
    }

    public void setId(long andIncrement) {
        id = andIncrement;
    }
}
