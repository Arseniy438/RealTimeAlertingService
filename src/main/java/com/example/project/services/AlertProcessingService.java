package com.example.project.services;

import com.example.project.domain.alerts.Alert;
import com.example.project.domain.events.Event;
import com.example.project.domain.rules.AlertRule;
import com.example.project.domain.repository.AlertRepository;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.Instant;
import java.util.Optional;

@Service
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
        Instant now = Instant.now(clock);
        for (AlertRule rule : alertRuleService.findMatching(event)) {
            processRule(rule, event, now);
        }
    }

    private void processRule(AlertRule rule, Event event, Instant now) {
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

    private void createNewAlert(AlertRule rule, Event event, Instant now) {
        Alert alert = Alert.create(rule, event, now);
        alertRepository.saveAlert(alert);
    }

    private void handleActiveAlert(Alert alert, Instant now) {
        if(alert.isInRecharge(now)) return;
        alert.retry(now);
        alert.setLastTriggeredAt(now);

        alertRepository.updateAlert(alert);
    }

}
