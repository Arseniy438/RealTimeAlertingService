package com.example.project.services;

import com.example.project.domain.events.Event;
import com.example.project.domain.events.EventField;
import com.example.project.domain.events.EventType;
import com.example.project.domain.repository.AlertRuleRepository;
import com.example.project.domain.rules.AlertRule;
import com.example.project.domain.rules.Severity;
import com.example.project.domain.rules.conditions.Condition;
import com.example.project.exceptions.RuleNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.Instant;
import java.util.List;

@Slf4j
@Service
public class AlertRuleService {

    private final AlertRuleRepository alertRuleRepository;
    private final Clock clock;

    public AlertRuleService(AlertRuleRepository alertRuleRepository, Clock clock) {
        this.alertRuleRepository = alertRuleRepository;
        this.clock = clock;
    }

    public List<AlertRule> findMatching(Event event) {
        var rules = alertRuleRepository.findRuleByEvent(event);
        log.debug("Found {} rules for eventId={} eventType={}", rules.size(), event.getId(), event.getType());
        return rules;
    }

    public AlertRule createAlertRule(String name, EventType type, EventField field, Severity severity, Condition condition) {
        return new AlertRule(name, type, field, severity, condition, Instant.now(clock));
    }

    public void addAlertRule(AlertRule rule) {
        AlertRule alertRule = alertRuleRepository.saveAlertRule(rule);
        log.info("Saved alert rule id={} name={} eventType={} field={}",
                alertRule.getId(),
                alertRule.getName(),
                alertRule.getSourceType(),
                alertRule.getSourceName());
    }

    public AlertRule getAlertRule(Long id) {
        return alertRuleRepository.getAlertRule(id).orElseThrow(() -> new RuleNotFoundException("Not found rule with id: " + id));
    }

    public void deleteAlertRule(Long id) {
        if (!alertRuleRepository.existsById(id)) {
            throw new RuleNotFoundException("Not found rule with id: " + id);
        }
        alertRuleRepository.deleteAlertRule(id);
        log.info("Deleted alert rule id={}", id);
    }
}
