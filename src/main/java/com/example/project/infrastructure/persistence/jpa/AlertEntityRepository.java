package com.example.project.infrastructure.persistence.jpa;

import com.example.project.domain.alerts.AlertStatus;
import com.example.project.infrastructure.persistence.AlertEntity;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AlertEntityRepository extends JpaRepository<AlertEntity, Long> {
    @Query("""
               select a from AlertEntity a
               join fetch a.rule
               join fetch a.event
               where a.rule.id = :ruleId
               and a.status = :status
            """)
    Optional<AlertEntity> findFirstByRuleIdAndStatus(Long ruleId, AlertStatus status);

    @Query("""
               select a from AlertEntity a
               join fetch a.rule
               join fetch a.event
            """)
    List<AlertEntity> findAllWithRuleAndEvent();
}
