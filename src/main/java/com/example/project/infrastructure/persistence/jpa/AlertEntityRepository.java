package com.example.project.infrastructure.persistence.jpa;

import com.example.project.domain.alerts.AlertStatus;
import com.example.project.infrastructure.persistence.AlertEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AlertEntityRepository extends JpaRepository<AlertEntity, Long> {
    Optional<AlertEntity> findFirstByRuleIdAndStatus(Long ruleId, AlertStatus status);
}
