package com.example.project.model.rules;

import com.example.project.model.events.Event;
import com.example.project.model.events.EventField;
import com.example.project.model.events.EventType;
import com.example.project.model.rules.conditions.Condition;

import java.time.Clock;
import java.time.Duration;
import java.time.LocalDateTime;

public class AlertRule {

    private Long id;
    private final String name;
    private boolean enabled;
    private final EventType eventType;
    private final EventField eventField;
    private final Severity severity;
    private final Condition condition;
    private final LocalDateTime createdAt;

    private String description;
    private int cooldown = 0;
    private int maxRetries = 3;
    private Duration comparisonWindow = Duration.ofMinutes(5);


    public AlertRule(String name, EventType eventType, EventField eventField, Severity severity, Condition condition) {
        if(!eventType.supports(eventField)){
            throw new IllegalStateException("Field " + eventField + " not supported by " + eventType);
        }
        this.name = name;
        this.condition = condition;
        this.enabled = true;
        this.eventType = eventType;
        this.eventField = eventField;
        this.severity = severity;
        this.createdAt = LocalDateTime.now();
    }


    public boolean matchesEventType(Event event){
        if(!enabled) return false;
        return this.eventType == event.getType();
    }

    public boolean shouldFire(Event event, Clock clock){
        if(!enabled) return false;
        if(event.getType() != eventType) return false;
        if(!isInComparisonWindow(event.getTimestamp(), LocalDateTime.now(clock))) return false;

        return event.getDouble(eventField)
                .map(condition::evaluate)
                .orElse(false);
    }

    /**
    *   Проверка на нахождение в "кулдауне"
    * */
    public boolean isInCooldown(LocalDateTime lastTriggeredAt, Clock clock){
        if(cooldown <= 0 || lastTriggeredAt == null){
            return false;
        }
        return lastTriggeredAt.plusSeconds(cooldown)
                .isAfter(LocalDateTime.now(clock));
    }

    /**
     * Проверка на нахождение в допустимом промежутке времени
     * @param eventTime время события
     * @param referenceTime опорное(текущее или контрольное) время
     */
    public boolean isInComparisonWindow(LocalDateTime eventTime, LocalDateTime referenceTime){
        return Duration.between(eventTime, referenceTime).compareTo(comparisonWindow) <= 0;
    }


    public void setCooldown(int cooldown) {
        this.cooldown = cooldown;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setMaxRetries(int maxRetries) {
        this.maxRetries = maxRetries;
    }

    public void setComparisonWindow(Long comparisonWindow) {
        this.comparisonWindow = Duration.ofMillis(comparisonWindow);
    }

    public Long getId() {return id;}

    public String getName() {return name;}

    public boolean isEnabled() {return enabled;}

    public EventType getSourceType() {return eventType;}

    public EventField getSourceName() {return eventField;}

    public Severity getSeverity() {return severity;}

    public Condition getCondition() {return condition;}

    public LocalDateTime getCreatedAt() {return createdAt;}

    public String getDescription() {return description;}

    public int getCooldown() {return cooldown;}

    public int getMaxRetries() {return maxRetries;}

    public Long getComparisonWindow() {return comparisonWindow.toMillis();}

    public void setId(long andIncrement) {
        id = andIncrement;
    }
}
