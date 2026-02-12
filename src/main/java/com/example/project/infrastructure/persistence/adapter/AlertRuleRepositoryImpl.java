package com.example.project.infrastructure.persistence.adapter;

import com.example.project.domain.events.Event;
import com.example.project.domain.repository.AlertRuleRepository;
import com.example.project.domain.rules.AlertRule;
import com.example.project.infrastructure.persistence.jpa.AlertRuleEntityRepository;
import com.example.project.infrastructure.persistence.mapper.AlertRuleMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class AlertRuleRepositoryImpl implements AlertRuleRepository {

    private final AlertRuleEntityRepository jpaRuleRepository;
    private final AlertRuleMapper mapper;

    public AlertRuleRepositoryImpl(AlertRuleEntityRepository jpaRuleRepository, AlertRuleMapper mapper) {
        this.jpaRuleRepository = jpaRuleRepository;
        this.mapper = mapper;
    }

    @Override
    public void saveAlertRule(AlertRule rule) {
        jpaRuleRepository.save(mapper.toEntity(rule));
    }

    @Override
    public Optional<AlertRule> getAlertRule(Long id) {
        return jpaRuleRepository.findById(id).map(mapper::toDomain);
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
