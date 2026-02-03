package com.example.project.domain.alerts;

import com.example.project.domain.rules.AlertRule;
import com.example.project.domain.events.Event;
import com.example.project.domain.rules.Severity;
import com.example.project.exceptions.UnsuitableStatusException;

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
    private LocalDateTime lastTriggeredAt;

    public Alert(AlertRule rule, Event event, String message, Severity severity, int retryCount, LocalDateTime now) {
        this.rule = rule;
        this.message = message;
        this.severity = severity;
        this.event = event;
        this.retryCount = retryCount;
        this.status = AlertStatus.NEW;
        this.createdAt = now;
        this.updatedAt = now;
        this.lastTriggeredAt = now;
    }

    public void activate(LocalDateTime now) {
        if (status == AlertStatus.NEW) {
            status = AlertStatus.ACTIVATED;
            touch(now);
        } else {
            throw new UnsuitableStatusException("Cannot activate alert from " + status);
        }
    }

    public static Alert create(AlertRule rule, Event event, LocalDateTime now) {
        Alert alert = new Alert(rule, event, rule.getDescription(), rule.getSeverity(), 0, now);
        alert.status = AlertStatus.ACTIVATED;
        return alert;
    }

    public void failed(LocalDateTime now) {
        status = AlertStatus.FAILED;
        touch(now);
    }

    public void acknowledged(LocalDateTime now) {
        if (status == AlertStatus.ACTIVATED) {
            status = AlertStatus.ACKNOWLEDGED;
            touch(now);
        } else {
            throw new UnsuitableStatusException("Cannot acknowledge alert from " + status);
        }

    }

    public void resolve(LocalDateTime now) {
        if (status == AlertStatus.ACTIVATED || status == AlertStatus.ACKNOWLEDGED) {
            status = AlertStatus.RESOLVED;
            touch(now);
        } else {
            throw new UnsuitableStatusException("Cannot resolve alert from " + status);
        }
    }

    public void retry(LocalDateTime now) {
        retryCount++;
        if (!canRetry(now)) {
            failed(now);
        }
        touch(now);
    }

    public boolean canRetry(LocalDateTime now) {
        if (retryCount < rule.getMaxRetries()) {
            return true;
        }
        return false;
    }

    public boolean isInRecharge(LocalDateTime now) {
        return rule.isInCooldown(lastTriggeredAt, now);
    }

    public boolean isFailed() {
        return status == AlertStatus.FAILED;
    }

    private void touch(LocalDateTime now) {
        updatedAt = now;
    }

    public LocalDateTime getLastTriggeredAt() {
        return lastTriggeredAt;
    }

    public void setLastTriggeredAt(LocalDateTime lastTriggeredAt) {
        this.lastTriggeredAt = lastTriggeredAt;
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
