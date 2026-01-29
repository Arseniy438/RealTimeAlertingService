package com.example.project.services;

import com.example.project.model.alerts.Alert;
import com.example.project.model.events.Event;
import com.example.project.model.events.EventType;
import com.example.project.model.rules.AlertRule;
import com.example.project.repository.IAlertRepository;

import java.time.Clock;
import java.util.List;
import java.util.Optional;

public class AlertProcessingService {

    private final IAlertRepository alertRepository;
    private final AlertRuleService alertRuleService;
    private final Clock clock;

    public AlertProcessingService(IAlertRepository alertRepository, AlertRuleService alertRuleService, Clock clock) {
        this.alertRepository = alertRepository;
        this.alertRuleService = alertRuleService;
        this.clock = clock;
    }


    public void process(Event event) {
        for (AlertRule rule : alertRuleService.findMatching(event)) {
            if (!rule.shouldFire(event, clock)) {continue;}

            Optional<Alert> active = alertRepository.findActiveByRule(rule);

            if (active.isPresent()) {
                handleActiveAlert(active.get(), rule);
            } else {
                createNewAlert(rule, event);
            }
        }
    }

    private void createNewAlert(AlertRule rule, Event event) {
        Alert alert = Alert.create(rule, event, clock);
        alertRepository.saveAlert(alert);
    }

    private void handleActiveAlert(Alert alert, AlertRule rule) {
        if(rule.isInCooldown(alert.getUpdatedAt(), clock)) return;
        if (!alert.canRetry()) return;

        alert.incrementRetry();
        alertRepository.updateAlert(alert);
    }

//    public Optional<Alert> getAlert(AlertRule rule){
//        return alertRepository.findActiveByRule(rule);
//    }
}
