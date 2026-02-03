package com.example.project.services;

import com.example.project.domain.alerts.Alert;
import com.example.project.domain.events.Event;
import com.example.project.domain.rules.AlertRule;
import com.example.project.domain.repository.AlertRepository;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Optional;

public class AlertProcessingService {

    private final AlertRepository alertRepository;
    private final AlertRuleService alertRuleService;
    private final Clock clock;

    public AlertProcessingService(AlertRepository alertRepository, AlertRuleService alertRuleService, Clock clock) {
        this.alertRepository = alertRepository;
        this.alertRuleService = alertRuleService;
        this.clock = clock;
    }

    public void process(Event event) {
        LocalDateTime now = LocalDateTime.now(clock);
        for (AlertRule rule : alertRuleService.findMatching(event)) {
            processRule(rule, event, now);
        }
    }

    private void processRule(AlertRule rule, Event event, LocalDateTime now) {
        if (!rule.shouldFire(event, now)) {
            return;
        }
        Optional<Alert> active = alertRepository.findActiveByRule(rule);

        if (active.isPresent()) {
            handleActiveAlert(active.get(), now);
        } else {
            createNewAlert(rule, event, now);
        }
    }

    private void createNewAlert(AlertRule rule, Event event, LocalDateTime now) {
        Alert alert = Alert.create(rule, event, now);
        alertRepository.saveAlert(alert);
    }

    private void handleActiveAlert(Alert alert, LocalDateTime now) {
        if(alert.isInRecharge(now)) return;
        alert.retry(now);
        alert.setLastTriggeredAt(now);

        alertRepository.updateAlert(alert);
    }

}
