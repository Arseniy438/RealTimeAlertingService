package com.example.project.infrastructure.persistence.jpa;

import com.example.project.domain.events.EventType;
import com.example.project.infrastructure.persistence.EventEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface EventEntityRepository extends JpaRepository<EventEntity, Long> {

    List<EventEntity> findAllByType(EventType type);

    List<EventEntity> findAllByOccurredAtAfter(Instant occurredAt);

    List<EventEntity> findAllByTypeAndOccurredAtAfter(EventType type, Instant occurredAt);

}
