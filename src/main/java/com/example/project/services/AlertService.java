package com.example.project.services;

import com.example.project.domain.alerts.Alert;
import com.example.project.domain.repository.AlertRepository;
import com.example.project.domain.rules.AlertRule;
import com.example.project.exceptions.AlertNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AlertService {

    private final Clock clock;
    private final AlertRepository alertRepository;
    private final ApplicationEventPublisher publisher;


    public void saveAlert(Alert alert) {

        Alert saved = alertRepository.saveAlert(alert);
        publisher.publishEvent(
                saved
        );
    }

    public void updateAlert(Alert alert) {
        publisher.publishEvent(
                alert
        );
        alertRepository.updateAlert(alert);
    }

    public Map<Long, Alert> findActiveByRules(Collection<AlertRule> rules) {
        if (rules.isEmpty()) {
            return Map.of();
        }
        return alertRepository.findActiveByRules(rules)
                .stream()
                .collect(Collectors.toMap(
                        alert -> alert.getRule().getId(),
                        Function.identity()
                ));
    }

    @Transactional
    public void switchToAcknowledged(Long id) {
        alertRepository.acknowledge(id);
        log.info("Acknowledged alert: id={}", id);
    }

    public Alert getAlert(Long id) {
        return alertRepository.getAlert(id).orElseThrow(() -> new AlertNotFoundException("Not found alert with id: " + id));
    }

    public List<Alert> getAllAlert() {
        return alertRepository.getAllAlert();
    }

    public void deleteAlert(Long id) {
        if (!alertRepository.existsById(id)) {
            throw new AlertNotFoundException("Not found alert with id: " + id);
        }
        alertRepository.deleteAlert(id);
        log.info("Deleted alert: id={}", id);
    }

    @Transactional
    public void processRetries() {

        List<Alert> alerts = alertRepository.findRetryableAlerts();

        for (Alert alert : alerts) {
            try {
                publisher.publishEvent(alert);
                alert.retry(Instant.now(clock));
                alertRepository.saveAlert(alert);
            } catch (Exception e) {
                System.out.println("Exception123");
            }
        }

    }
}
