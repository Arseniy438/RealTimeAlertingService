package com.example.project;

import com.example.project.domain.repository.AlertRepository;
import com.example.project.domain.repository.AlertRuleRepository;
import com.example.project.domain.events.Event;
import com.example.project.domain.events.EventField;
import com.example.project.domain.events.EventType;
import com.example.project.domain.rules.Severity;
import com.example.project.domain.rules.conditions.Condition;
import com.example.project.domain.rules.conditions.LessThanCondition;
import com.example.project.infrastructure.InMemoryAlertRepository;
import com.example.project.infrastructure.InMemoryAlertRuleRepository;
import com.example.project.services.AlertProcessingService;
import com.example.project.services.AlertRuleService;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class RealTimeAlertingServiceApplication {
    public static void main(String[] args) throws InterruptedException {
//        SpringApplication.run(RealTimeAlertingServiceApplication.class, args);
        AlertRuleRepository alertRuleRepository = new InMemoryAlertRuleRepository();
        AlertRepository alertRepository = new InMemoryAlertRepository();

        Clock clock = Clock.systemUTC();

        AlertRuleService ruleService = new AlertRuleService(alertRuleRepository, clock);
        AlertProcessingService alertProcessingService = new AlertProcessingService(alertRepository, ruleService, Clock.systemUTC());

        Condition lessThanCondition = new LessThanCondition(11.0);
        ruleService.createAlertRule("cpuCheck", EventType.CPU, EventField.CPU_USAGE, Severity.INFO, lessThanCondition);

        Map<EventField, Object> map = new HashMap<>();
        map.put(EventField.CPU_USAGE, 10.0);

        Event event = new Event(EventType.CPU, LocalDateTime.now(), map);
        event.setId(3L);

        alertProcessingService.process(event);

    }
}
