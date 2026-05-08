package com.example.project.services;

import com.example.project.domain.events.Event;
import com.example.project.domain.events.EventType;
import com.example.project.domain.repository.EventRepository;
import com.example.project.exceptions.EventNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final AlertProcessingService alertProcessingService;

    public Optional<Event> getEventById(Long id){
        return eventRepository.findById(id);
    }

    @Transactional
    public void saveEvent(Event event){
        eventRepository.save(event);
        log.info("Saved event id={} eventType={}",
                event.getId(),
                event.getType());
        alertProcessingService.process(event);
    }

    public List<Event> findEventByType(EventType type){
        return eventRepository.findByType(type);
    }

    public void deleteEvent(Long id){
        if(!eventRepository.existsById(id)){
            throw new EventNotFoundException("Not found event with id: " + id);
        }
        eventRepository.deleteEvent(id);
        log.info("Deleted event: id={}", id);
    }

}
