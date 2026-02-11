package com.example.project.domain.alerts;

import com.example.project.domain.rules.AlertRule;
import com.example.project.domain.events.Event;
import com.example.project.domain.rules.Severity;
import com.example.project.exceptions.UnsuitableStatusException;

import java.time.Instant;

public class Alert {

    private Long id;
    private final AlertRule rule;
    private String message;
    private final Instant createdAt;
    private Instant updatedAt;
    private AlertStatus status;
    private Severity severity;
    private final Event event;
    private int retryCount; // сколько раз ретраили
    private Instant lastTriggeredAt;

    public Alert(AlertRule rule, Event event, String message, Severity severity, int retryCount, Instant now) {
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

    public Alert(Long id, AlertRule rule, Event event, Severity severity, int retryCount, Instant now) {
        this.id = id;
        this.rule = rule;
        this.severity = severity;
        this.event = event;
        this.retryCount = retryCount;
        this.status = AlertStatus.NEW;
        this.createdAt = now;
        this.updatedAt = now;
        this.lastTriggeredAt = now;
    }


    public void activate(Instant now) {
        if (status == AlertStatus.NEW) {
            status = AlertStatus.ACTIVATED;
            touch(now);
        } else {
            throw new UnsuitableStatusException("Cannot activate alert from " + status);
        }
    }

    public static Alert create(AlertRule rule, Event event, Instant now) {
        Alert alert = new Alert(rule, event, rule.getDescription(), rule.getSeverity(), 0, now);
        alert.status = AlertStatus.ACTIVATED;
        return alert;
    }

    public void failed(Instant now) {
        status = AlertStatus.FAILED;
        touch(now);
    }

    public void acknowledged(Instant now) {
        if (status == AlertStatus.ACTIVATED) {
            status = AlertStatus.ACKNOWLEDGED;
            touch(now);
        } else {
            throw new UnsuitableStatusException("Cannot acknowledge alert from " + status);
        }

    }

    public void resolve(Instant now) {
        if (status == AlertStatus.ACTIVATED || status == AlertStatus.ACKNOWLEDGED) {
            status = AlertStatus.RESOLVED;
            touch(now);
        } else {
            throw new UnsuitableStatusException("Cannot resolve alert from " + status);
        }
    }

    public void retry(Instant now) {
        retryCount++;
        if (!canRetry(now)) {
            failed(now);
        }
        touch(now);
    }

    public boolean canRetry(Instant now) {
        if (retryCount < rule.getMaxRetries()) {
            return true;
        }
        return false;
    }

    public boolean isInRecharge(Instant now) {
        return rule.isInCooldown(lastTriggeredAt, now);
    }

    public boolean isFailed() {
        return status == AlertStatus.FAILED;
    }

    private void touch(Instant now) {
        updatedAt = now;
    }

    public Instant getLastTriggeredAt() {
        return lastTriggeredAt;
    }

    public void setLastTriggeredAt(Instant lastTriggeredAt) {
        this.lastTriggeredAt = lastTriggeredAt;
    }

    public Instant getUpdatedAt() {
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

    public Instant getCreatedAt() {
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
