package com.example.project.services;

import com.example.project.domain.alerts.Alert;
import com.example.project.domain.events.Event;
import com.example.project.domain.rules.AlertRule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.Instant;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class AlertProcessingService {

    private final AlertService alertService;
    private final AlertRuleService alertRuleService;
    private final Clock clock;

    @Transactional
    public void process(Event event) {
        Instant now = Instant.now(clock);
        var matchingRules = alertRuleService.findMatching(event);
        log.info("Processing event id={} type={} occurredAt={} with {} candidate rules",
                event.getId(), event.getType(), event.getOccurredAt(), matchingRules.size());

        if (matchingRules.isEmpty()) {
            return;
        }

        Map<Long, Alert> active = alertService.findActiveByRules(matchingRules);

        for (AlertRule rule : matchingRules) {
            processRule(rule, event, now, active);
        }
    }

    private void processRule(AlertRule rule, Event event, Instant now, Map<Long, Alert> activeAlerts) {
        if (!rule.shouldFire(event, now)) {
            log.debug("Rule did not fire: ruleId={} ruleName={} eventId={} eventType={}",
                    rule.getId(), rule.getName(), event.getId(), event.getType());
            return;
        }
        log.info("Rule fired: ruleId={} ruleName={} eventId={} eventType={}",
                rule.getId(), rule.getName(), event.getId(), event.getType());

        Alert activeAlert = activeAlerts.get(rule.getId());

        if (activeAlert != null) {
            handleActiveAlert(activeAlert, now);
        } else {
            createNewAlert(rule, event, now);
        }
    }

    private void createNewAlert(AlertRule rule, Event event, Instant now) {
        Alert alert = Alert.create(rule, event, now);
        alertService.saveAlert(alert);

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

        alertService.updateAlert(alert);

        log.info("Updated active alert: alertId={} ruleId={} retryCount={}=>{} status={}=>{}",
                alert.getId(),
                alert.getRule().getId(),
                previousRetryCount,
                alert.getRetryCount(),
                previousStatus,
                alert.getStatus());
    }

}
