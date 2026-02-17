package com.example.project;

import com.example.project.domain.events.Event;
import com.example.project.domain.events.EventField;
import com.example.project.domain.events.EventType;
import com.example.project.domain.repository.AlertRepository;
import com.example.project.domain.repository.EventRepository;
import com.example.project.domain.rules.AlertRule;
import com.example.project.domain.rules.Severity;
import com.example.project.domain.rules.conditions.LessThanCondition;
import com.example.project.services.AlertProcessingService;
import com.example.project.services.AlertRuleService;
import com.example.project.services.AlertService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.time.Clock;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class RealTimeAlertingServiceApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(RealTimeAlertingServiceApplication.class, args);

        AlertRepository alertRepository = context.getBean(AlertRepository.class);
        EventRepository eventRepository = context.getBean(EventRepository.class);
        AlertProcessingService processingService = context.getBean(AlertProcessingService.class);
        AlertRuleService ruleService = context.getBean(AlertRuleService.class);
        Clock clock = context.getBean(Clock.class);
        AlertService alertService = context.getBean(AlertService.class);
//
//        Map<EventField, Object> map = new HashMap<>();
//        map.put(EventField.DISK_FREE, 10.0);
//        Event event = new Event(2L, EventType.DISK, Instant.now(clock), map);
//        eventRepository.save(event);
//
//        AlertRule rule = new AlertRule(2L ,"Disk",true, EventType.DISK, EventField.DISK_FREE, Severity.INFO, new LessThanCondition(50.0), Instant.now(clock));
//
//        ruleService.addAlertRule(rule);
//        processingService.process(event);
        alertService.deleteAlert(2L);
        ruleService.deleteAlertRule(2L);


    }
}
