package com.example.project.infrastructure.repository;

import com.example.project.domain.events.Event;
import com.example.project.domain.events.EventType;
import com.example.project.domain.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;


@Repository
@RequiredArgsConstructor
public class InMemoryEventRepository implements EventRepository {

    private final Map<Long, Event> eventMap = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public void save(Event event) {
        if (event.getId() == null) {
            event.setId(idGenerator.getAndIncrement());
        }
        eventMap.put(event.getId(), event);
    }

    @Override
    public Optional<Event> findById(Long id) {
        return Optional.ofNullable(eventMap.get(id));
    }

    @Override
    public List<Event> findByType(EventType type) {
        return eventMap.values().stream().filter(e -> e.getType() == type).toList();
    }

    @Override
    public void deleteEvent(Long id) {
        if (eventMap.containsKey(id)) {
            eventMap.remove(id);
        }
    }
}
