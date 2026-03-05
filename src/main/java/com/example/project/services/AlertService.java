package com.example.project.services;

import com.example.project.domain.alerts.Alert;
import com.example.project.domain.events.EventType;
import com.example.project.domain.repository.AlertRepository;
import com.example.project.domain.rules.AlertRule;
import com.example.project.domain.rules.Severity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AlertService {

    private final Clock clock;
    private final AlertRepository alertRepository;

    public void processEvent(Alert alert) {
        Instant now = Instant.now(clock);

        if (!alert.canRetry() && !alert.isFailed()) {
            alert.failed(now);
        }
        if (alert.getRule().getSeverity() == Severity.CRITICAL) {
            alert.getRule().setMaxRetries(5);
        }

        if (alert.getEvent().getType() == EventType.RECOVERY) {
            alert.resolve(now);
        }
    }

    public void saveAlert(Alert alert) {
        alertRepository.saveAlert(alert);
    }

    public void updateAlert(Alert alert) {
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

    public Alert getAlert(Long id) {
        return alertRepository.getAlert(id).orElseThrow(() -> new NoSuchElementException("Not found alert with id: " + id));
    }

    public List<Alert> getAllAlert() {
        return alertRepository.getAllAlert();
    }

    public void deleteAlert(Long id) {
        alertRepository.deleteAlert(id);
        log.info("Deleted alert: id={}", id);
    }

}
