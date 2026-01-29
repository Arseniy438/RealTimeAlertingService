package com.example.project.model.alerts;

import com.example.project.model.rules.AlertRule;
import com.example.project.model.events.Event;
import com.example.project.model.rules.Severity;
import com.example.project.services.AlertRuleService;
//import jakarta.persistence.*;

import java.time.Clock;
import java.time.LocalDateTime;

//@Entity
//@Table(name = "alerts")
public class Alert {
    //    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private AlertRule rule;
    private String message;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private AlertStatus status;
    private Severity severity;
    private Event event;
    private int retryCount;


    public Alert() {}

    public Alert(AlertRule rule, Event event, String message, Severity severity, int retryCount) {
        this.rule = rule;
        this.message = message;
        this.severity = severity;
        this.event = event;
        this.retryCount = retryCount;
        this.status = AlertStatus.NEW;
        this.createdAt = LocalDateTime.now(Clock.systemUTC());
        this.updatedAt = LocalDateTime.now(Clock.systemUTC());
    }

    public void activate() {
        if (status != AlertStatus.NEW) {
            throw new IllegalStateException("Cannot activate alert from " + status);
        }
        status = AlertStatus.ACTIVE;
        touch(Clock.systemUTC());
    }

    public static Alert create(AlertRule rule, Event event, Clock clock) {
        Alert alert = new Alert(rule, event, rule.getDescription(), rule.getSeverity(), 0);
        alert.status = AlertStatus.ACTIVE;
        alert.createdAt = LocalDateTime.now(clock);
        alert.updatedAt = alert.createdAt;
        return alert;
    }

    public void failed() {
        if (status != AlertStatus.FAILED) {
            status = AlertStatus.FAILED;
            touch(Clock.systemUTC());
        }
    }

    public void acknowledged() {
        if (status != AlertStatus.ACTIVE && status != AlertStatus.ACKNOWLEDGED) {
            throw new IllegalStateException("Cannot activate alert from " + status);
        }
        status = AlertStatus.ACKNOWLEDGED;
        touch(Clock.systemUTC());
    }

    public void resolve() {
        if (status != AlertStatus.ACTIVE && status != AlertStatus.ACKNOWLEDGED) {
            throw new IllegalStateException("Cannot resolve alert from " + status);
        }
        status = AlertStatus.RESOLVED;
        touch(Clock.systemUTC());
    }

    public boolean canRetry() {
        retryCount++;
        if (retryCount >= rule.getMaxRetries()) {
            failed();
            return false;
        }
        return true;
    }

    public void incrementRetry() {
        retryCount++;
    }

    private void touch(Clock clock) {
        updatedAt = LocalDateTime.now(clock);
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
