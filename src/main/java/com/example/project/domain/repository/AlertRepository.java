package com.example.project.domain.repository;

import com.example.project.domain.alerts.Alert;
import com.example.project.domain.rules.AlertRule;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface AlertRepository {

    void saveAlert(Alert alert);

    void updateAlert(Alert alert);

    Optional<Alert> getAlert(Long id);

    void deleteAlert(Long id);

    Optional<Alert> findActiveByRule(AlertRule rule);

    List<Alert> findActiveByRules(Collection<AlertRule> rules);

    List<Alert> getAllAlert();

    boolean existsById(Long id);
}
