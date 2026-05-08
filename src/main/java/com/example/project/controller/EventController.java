package com.example.project.controller;

import com.example.project.domain.events.EventType;
import com.example.project.dto.request.EventRequest;
import com.example.project.dto.request.EventRequestMapper;
import com.example.project.dto.response.EventResponse;
import com.example.project.dto.response.EventResponseMapper;
import com.example.project.services.EventService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@Tag(name = "events")
@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;
    private final EventResponseMapper eventResponseMapper;
    private final EventRequestMapper eventRequestMapper;

    @GetMapping("/{id}")
    public EventResponse getEvent(@PathVariable Long id){
        return eventResponseMapper.toEventResponse(eventService.getEventById(id).orElseThrow(NoSuchElementException::new));
    }

    @GetMapping("/type/{type}")
    public List<EventResponse> getEventByType(@PathVariable String type){
        EventType eventType = EventType.valueOf(type);
        return eventService.findEventByType(eventType).stream().map(eventResponseMapper::toEventResponse).toList();
    }

    @PostMapping("/create")
    public void createEvent(@RequestBody EventRequest eventRequest){
        eventService.saveEvent(eventRequestMapper.toDomain(eventRequest));
    }

    @DeleteMapping("/delete/{id}")
    public void deleteEvent(@PathVariable Long id){
        eventService.deleteEvent(id);
    }
}
