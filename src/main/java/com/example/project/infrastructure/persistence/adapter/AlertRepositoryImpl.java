package com.example.project.infrastructure.persistence.adapter;

import com.example.project.domain.alerts.Alert;
import com.example.project.domain.alerts.AlertStatus;
import com.example.project.domain.repository.AlertRepository;
import com.example.project.domain.rules.AlertRule;
import com.example.project.infrastructure.persistence.jpa.AlertEntityRepository;
import com.example.project.infrastructure.persistence.mapper.AlertMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class AlertRepositoryImpl implements AlertRepository {

    private final AlertEntityRepository jpaRepository;
    private final AlertMapper alertMapper;

    public AlertRepositoryImpl(AlertEntityRepository jpaRepository, AlertMapper alertMapper) {
        this.jpaRepository = jpaRepository;
        this.alertMapper = alertMapper;
    }

    @Override
    public void saveAlert(Alert alert) {
        jpaRepository.save(alertMapper.toEntity(alert));
    }

    @Override
    public void updateAlert(Alert alert) {
        jpaRepository.saveAndFlush(alertMapper.toEntity(alert));
    }

    @Override
    public Optional<Alert> getAlert(Long id) {
        return jpaRepository.findById(id).map(alertMapper::toDomain);
    }

    @Override
    public void deleteAlert(Long id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public Optional<Alert> findActiveByRule(AlertRule rule) {
        if (rule.getId() == null) {
            return Optional.empty();
        }
        return jpaRepository.findFirstByRuleIdAndStatus(rule.getId(), AlertStatus.ACTIVATED).map(alertMapper::toDomain);
    }

    @Override
    public List<Alert> getAllAlert() {
        return jpaRepository.findAll().stream().map(alertMapper::toDomain).toList();
    }
}
