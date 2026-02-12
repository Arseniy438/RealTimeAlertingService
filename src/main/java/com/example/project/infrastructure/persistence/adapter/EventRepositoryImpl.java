package com.example.project.infrastructure.persistence.adapter;

import com.example.project.domain.events.Event;
import com.example.project.domain.events.EventType;
import com.example.project.domain.repository.EventRepository;
import com.example.project.infrastructure.persistence.jpa.EventEntityRepository;
import com.example.project.infrastructure.persistence.mapper.EventMapper;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public class EventRepositoryImpl implements EventRepository {

    private final EventEntityRepository jpaRepository;
    private final EventMapper mapper;

    public EventRepositoryImpl(EventEntityRepository jpaRepository, EventMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public void save(Event event) {
        jpaRepository.save(mapper.toEntity(event));
    }

    @Override
    public Optional<Event> findById(Long id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<Event> findByType(EventType type) {
        return jpaRepository.findAllByType(type).stream().map(mapper::toDomain).toList();
    }

    public List<Event> findAllByTypeAndOccurredAtAfter(EventType type, Instant occurredAt) {
        return jpaRepository.findAllByTypeAndOccurredAtAfter(type, occurredAt).stream().map(mapper::toDomain).toList();
    }

    public List<Event> findAllByOccurredAtAfter(Instant occurredAt) {
        return jpaRepository.findAllByOccurredAtAfter(occurredAt).stream().map(mapper::toDomain).toList();
    }

    @Override
    public void deleteEvent(Long id) {
        jpaRepository.deleteById(id);
    }
}
