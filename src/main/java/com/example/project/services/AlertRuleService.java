package com.example.project.services;


import com.example.project.domain.events.Event;
import com.example.project.domain.events.EventField;
import com.example.project.domain.events.EventType;
import com.example.project.domain.rules.AlertRule;
import com.example.project.domain.rules.Severity;
import com.example.project.domain.rules.conditions.Condition;
import com.example.project.domain.repository.AlertRuleRepository;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class AlertRuleService {

    private final AlertRuleRepository alertRuleRepository;
    private static Clock clock;

    public AlertRuleService(AlertRuleRepository alertRuleRepository, Clock clock) {
        this.alertRuleRepository = alertRuleRepository;
        AlertRuleService.clock = clock;
    }

    public List<AlertRule> findMatching(Event event) {
        return alertRuleRepository.findRuleByEvent(event);
    }

    public static AlertRule createAlertRule(String name, EventType type, EventField field, Severity severity, Condition condition) {
        AlertRule rule = new AlertRule(name, type, field, severity, condition, LocalDateTime.now(clock));
        return rule;
    }

    public void addAlertRule(AlertRule alertRule) {
        alertRuleRepository.addAlertRule(alertRule);
    }

    public Optional<AlertRule> getAlertRule(Long id) {
        return alertRuleRepository.getAlertRule(id);
    }

    public void deleteAlertRule(Long id) {
        alertRuleRepository.deleteAlertRule(id);
    }

}
