package com.example.project.services;


import com.example.project.model.events.Event;
import com.example.project.model.events.EventField;
import com.example.project.model.events.EventType;
import com.example.project.model.rules.AlertRule;
import com.example.project.model.rules.Severity;
import com.example.project.model.rules.conditions.Condition;
import com.example.project.repository.IAlertRuleRepository;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class AlertRuleService {

    private final IAlertRuleRepository alertRuleRepository;
    private static Clock clock;

    public AlertRuleService(IAlertRuleRepository alertRuleRepository, Clock clock) {
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
