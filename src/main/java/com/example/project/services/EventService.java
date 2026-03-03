package com.example.project.services;

import com.example.project.domain.events.Event;
import com.example.project.domain.events.EventType;
import com.example.project.domain.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;

    public Optional<Event> getEventById(Long id){
        return eventRepository.findById(id);
    }

    public void saveEvent(Event event){
        eventRepository.save(event);
        log.info("Saved event id={} eventType={}",
                event.getId(),
                event.getType());
    }

    public List<Event> findEventByType(EventType type){
        return eventRepository.findByType(type);
    }

    public void deleteEvent(Long id){
        eventRepository.deleteEvent(id);
        log.info("Deleted event: id={}", id);
    }

}
