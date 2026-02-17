package com.example.project.domain.rules;

import com.example.project.domain.events.Event;
import com.example.project.domain.events.EventField;
import com.example.project.domain.events.EventType;
import com.example.project.domain.rules.conditions.Condition;
import com.example.project.exceptions.NotSupportedTypeException;
import lombok.Getter;

import java.time.Duration;
import java.time.Instant;

@Getter
public class AlertRule {

    private Long id;
    private final String name;
    private boolean enabled;
    private final EventType eventType;
    private final EventField eventField;
    private final Severity severity;
    private final Condition condition;
    private final Instant createdAt;

    private String description;
    private int cooldownInSeconds = 0;
    private int maxRetries = 3;
    private Duration comparisonWindow = Duration.ofMinutes(5);


    public AlertRule(String name, EventType eventType, EventField eventField, Severity severity, Condition condition, Instant now) {
        if (!eventType.supports(eventField)) {
            throw new NotSupportedTypeException("Field " + eventField + " not supported by " + eventType);
        }
        this.name = name;
        this.condition = condition;
        this.enabled = true;
        this.eventType = eventType;
        this.eventField = eventField;
        this.severity = severity;
        this.createdAt = now;
    }

    public AlertRule(Long id, String name, boolean enabled, EventType eventType, EventField eventField, Severity severity, Condition condition, Instant createdAt) {
        this.id = id;
        this.name = name;
        this.enabled = enabled;
        this.eventType = eventType;
        this.eventField = eventField;
        this.severity = severity;
        this.condition = condition;
        this.createdAt = createdAt;
    }

    public boolean isMatchesEventType(Event event) {
        if (!enabled) return false;
        return this.eventType == event.getType();
    }

    public boolean shouldFire(Event event, Instant now) {
        if (!enabled) return false;
        if (event.getType() != eventType) return false;
        if (!isInComparisonWindow(event.getOccurredAt(), now)) return false;

        return event.getDouble(eventField)
                .map(condition::evaluate)
                .orElse(false);
    }

    /**
     * Проверка на нахождение в "кулдауне"
     */
    public boolean isInCooldown(Instant lastTriggeredAt, Instant now) {
        if (cooldownInSeconds <= 0 || lastTriggeredAt == null) {
            return false;
        }
        return lastTriggeredAt.plusSeconds(cooldownInSeconds).isAfter(now);
    }

    /**
     * Проверка на нахождение в допустимом промежутке времени
     *
     * @param eventTime     время события
     * @param referenceTime опорное(текущее или контрольное) время
     */
    public boolean isInComparisonWindow(Instant eventTime, Instant referenceTime) {
        return Duration.between(eventTime, referenceTime).compareTo(comparisonWindow) <= 0;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setCooldownInSeconds(int cooldownInSeconds) {
        this.cooldownInSeconds = cooldownInSeconds;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setMaxRetries(int maxRetries) {
        this.maxRetries = maxRetries;
    }

    public void setComparisonWindow(Long comparisonWindow) {
        this.comparisonWindow = Duration.ofSeconds(comparisonWindow);
    }

    public EventType getSourceType() {
        return eventType;
    }

    public EventField getSourceName() {
        return eventField;
    }

    public Long getComparisonWindow() {
        return comparisonWindow.toSeconds();
    }

    public void setId(long andIncrement) {
        id = andIncrement;
    }
}
