package com.example.project.infrastructure.persistence.jpa;

import com.example.project.domain.events.EventField;
import com.example.project.domain.events.EventType;
import com.example.project.domain.rules.Severity;
import com.example.project.infrastructure.persistence.AlertRuleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlertRuleEntityRepository extends JpaRepository<AlertRuleEntity, Long> {

    List<AlertRuleEntity> findAllByType(EventType type);

    List<AlertRuleEntity> findAllByField(EventField field);

    List<AlertRuleEntity> findAllBySeverity(Severity severity);
}
