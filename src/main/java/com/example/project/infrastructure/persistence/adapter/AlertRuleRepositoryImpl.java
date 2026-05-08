package com.example.project.infrastructure.persistence.adapter;

import com.example.project.domain.events.Event;
import com.example.project.domain.repository.AlertRuleRepository;
import com.example.project.domain.rules.AlertRule;
import com.example.project.infrastructure.persistence.AlertRuleEntity;
import com.example.project.infrastructure.persistence.jpa.AlertRuleEntityRepository;
import com.example.project.infrastructure.persistence.mapper.AlertRuleMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class AlertRuleRepositoryImpl implements AlertRuleRepository {

    private final AlertRuleEntityRepository jpaRuleRepository;
    private final AlertRuleMapper mapper;

    @Override
    public AlertRule saveAlertRule(AlertRule rule) {
        AlertRuleEntity saved = jpaRuleRepository.save(mapper.toEntity(rule));
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<AlertRule> getAlertRule(Long id) {
        return jpaRuleRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public boolean existsById(Long id) {
        return jpaRuleRepository.existsById(id);
    }

    @Override
    public void deleteAlertRule(Long id) {
        jpaRuleRepository.deleteById(id);
    }

    @Override
    public List<AlertRule> findRuleByEvent(Event event) {
        return jpaRuleRepository.findAllByType(event.getType()).stream().map(mapper::toDomain).toList();
    }
}
