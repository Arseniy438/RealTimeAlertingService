package com.example.project.controller;

import com.example.project.domain.events.Event;
import com.example.project.domain.events.EventType;
import com.example.project.services.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @GetMapping("/{id}")
    public Event getEvent(@PathVariable Long id){
        return eventService.getEventById(id).orElseThrow(NoSuchElementException::new);
    }

    @GetMapping("/type/{type}")
    public List<Event> getEventByType(@PathVariable String type){
        EventType eventType = EventType.valueOf(type);
        return eventService.findEventByType(eventType);
    }


}
