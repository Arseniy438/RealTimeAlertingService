package com.example.project.infrastructure.persistence.jpa;

import com.example.project.domain.alerts.AlertStatus;
import com.example.project.infrastructure.persistence.AlertEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
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

    @Query("""
       select a
       from AlertEntity a
       join fetch a.rule
       join fetch a.event
       where a.rule.id in :ruleIds
       and a.status = :status
       """)
    List<AlertEntity> findActiveByRuleIds(@Param("ruleIds") Collection<Long> ruleIds, @Param("status") AlertStatus status);

    @Modifying
    @Query("""
    update AlertEntity a
    set a.status = 'ACKNOWLEDGED'
    where a.id = :id
""")
    void acknowledge(@Param("id") Long id);

    @Query("""
        select a from AlertEntity a
        where a.status = 'ACTIVATED'
        """)
    List<AlertEntity> findRetryableAlerts();
}
