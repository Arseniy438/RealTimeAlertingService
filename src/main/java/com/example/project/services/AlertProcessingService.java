package com.example.project.services;

import com.example.project.domain.alerts.Alert;
import com.example.project.domain.events.Event;
import com.example.project.domain.repository.AlertRepository;
import com.example.project.domain.rules.AlertRule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.Instant;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AlertProcessingService {

    private final AlertRepository alertRepository;
    private final AlertRuleService alertRuleService;
    private final Clock clock;

    @Transactional
    public void process(Event event) {
        Instant now = Instant.now(clock);
        var matchingRules = alertRuleService.findMatching(event);
        log.info("Processing event id={} type={} occurredAt={} with {} candidate rules",
                event.getId(), event.getType(), event.getOccurredAt(), matchingRules.size());
        for (AlertRule rule : matchingRules) {
            processRule(rule, event, now);
        }
    }

    private void processRule(AlertRule rule, Event event, Instant now) {
        if (!rule.shouldFire(event, now)) {
            log.debug("Rule did not fire: ruleId={} ruleName={} eventId={} eventType={}",
                    rule.getId(), rule.getName(), event.getId(), event.getType());
            return;
        }
        log.info("Rule fired: ruleId={} ruleName={} eventId={} eventType={}",
                rule.getId(), rule.getName(), event.getId(), event.getType());
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

        log.info("Created new alert for ruleId={} eventId={} status={} severity={}",
                rule.getId(), event.getId(), alert.getStatus(), alert.getSeverity());
    }

    private void handleActiveAlert(Alert alert, Instant now) {
        if (alert.isInRecharge(now)) {
            log.debug("Skipping retry due to cooldown: alertId={} ruleId={} lastTriggeredAt={}",
                    alert.getId(), alert.getRule().getId(), alert.getLastTriggeredAt());

            return;
        }
        var previousStatus = alert.getStatus();
        var previousRetryCount = alert.getRetryCount();

        alert.retry(now);
        alert.setLastTriggeredAt(now);

        alertRepository.updateAlert(alert);

        log.info("Updated active alert: alertId={} ruleId={} retryCount={}=>{} status={}=>{}",
                alert.getId(),
                alert.getRule().getId(),
                previousRetryCount,
                alert.getRetryCount(),
                previousStatus,
                alert.getStatus());
    }

}
