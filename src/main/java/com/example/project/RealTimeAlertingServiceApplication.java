package com.example.project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RealTimeAlertingServiceApplication {
    public static void main(String[] args) throws InterruptedException {
        SpringApplication.run(RealTimeAlertingServiceApplication.class, args);
//        AlertRuleRepository alertRuleRepository = new InMemoryAlertRuleRepository();
//        AlertRepository alertRepository = new InMemoryAlertRepository();
//
//        Clock clock = Clock.systemUTC();
//
//        AlertRuleService ruleService = new AlertRuleService(alertRuleRepository, clock);
//        AlertProcessingService alertProcessingService = new AlertProcessingService(alertRepository, ruleService, Clock.systemUTC());
//
//        Condition lessThanCondition = new LessThanCondition(11.0);
//        ruleService.createAlertRule("cpuCheck", EventType.CPU, EventField.CPU_USAGE, Severity.INFO, lessThanCondition);
//
//        Map<EventField, Object> map = new HashMap<>();
//        map.put(EventField.CPU_USAGE, 10.0);
//
//        Event event = new Event(EventType.CPU, LocalDateTime.now(), map);
//        event.setId(3L);
//
//        alertProcessingService.process(event);

    }
}
