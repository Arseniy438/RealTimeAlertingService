package com.example.project.infrastructure.persistence.adapter;

import com.example.project.domain.alerts.Alert;
import com.example.project.domain.alerts.AlertStatus;
import com.example.project.domain.events.Event;
import com.example.project.domain.repository.AlertRepository;
import com.example.project.domain.rules.AlertRule;
import com.example.project.infrastructure.persistence.AlertEntity;
import com.example.project.infrastructure.persistence.AlertRuleEntity;
import com.example.project.infrastructure.persistence.EventEntity;
import com.example.project.infrastructure.persistence.jpa.AlertEntityRepository;
import com.example.project.infrastructure.persistence.jpa.AlertRuleEntityRepository;
import com.example.project.infrastructure.persistence.jpa.EventEntityRepository;
import com.example.project.infrastructure.persistence.mapper.AlertMapper;
import com.example.project.infrastructure.persistence.mapper.AlertRuleMapper;
import com.example.project.infrastructure.persistence.mapper.EventMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class AlertRepositoryImpl implements AlertRepository {

    private final AlertEntityRepository jpaRepository;
    private final AlertRuleEntityRepository ruleRepository;
    private final EventEntityRepository eventRepository;
    private final AlertMapper alertMapper;
    private final AlertRuleMapper alertRuleMapper;
    private final EventMapper eventMapper;


    @Override
    public void acknowledge(Long id) {
        jpaRepository.acknowledge(id);
    }

    @Override
    public Alert saveAlert(Alert alert) {
        AlertEntity entity = mapWithResolvedRelations(alert);
        return alertMapper.toDomain(jpaRepository.save(entity));
    }

    @Override
    public List<Alert> findRetryableAlerts() {
        return jpaRepository.findRetryableAlerts()
                .stream()
                .map(alertMapper::toDomain)
                .toList();
    }

    @Override
    public void updateAlert(Alert alert) {
        AlertEntity entity = mapWithResolvedRelations(alert);
        jpaRepository.saveAndFlush(entity);
    }

    @Override
    public Optional<Alert> getAlert(Long id) {
        return jpaRepository.findById(id)
                .map(alertMapper::toDomain);
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

        return jpaRepository
                .findFirstByRuleIdAndStatus(rule.getId(), AlertStatus.ACTIVATED)
                .map(alertMapper::toDomain);
    }

    @Override
    public boolean existsById(Long id) {
        return jpaRepository.existsById(id);
    }

    @Override
    public List<Alert> findActiveByRules(Collection<AlertRule> rules) {
        if (rules.isEmpty()) {
            return List.of();
        }

        List<Long> ruleIds = rules.stream()
                .map(AlertRule::getId)
                .toList();

        return jpaRepository.findActiveByRuleIds(ruleIds, AlertStatus.ACTIVATED)
                .stream()
                .map(alertMapper::toDomain)
                .toList();
    }

    @Override
    public List<Alert> getAllAlert() {
        return jpaRepository.findAllWithRuleAndEvent()
                .stream()
                .map(alertMapper::toDomain)
                .toList();
    }


    private AlertEntity mapWithResolvedRelations(Alert alert) {

        AlertRuleEntity ruleEntity = resolveRuleEntity(alert.getRule());
        EventEntity eventEntity = resolveEventEntity(alert.getEvent());

        return alertMapper.toEntity(alert, ruleEntity, eventEntity);
    }

    private AlertRuleEntity resolveRuleEntity(AlertRule rule) {
        if (rule.getId() != null) {
            return ruleRepository.getReferenceById(rule.getId());
        }
        return ruleRepository.save(alertRuleMapper.toEntity(rule));
    }

    private EventEntity resolveEventEntity(Event event) {
        if (event.getId() != null) {
            return eventRepository.getReferenceById(event.getId());
        }
        return eventRepository.save(eventMapper.toEntity(event));
    }
}
