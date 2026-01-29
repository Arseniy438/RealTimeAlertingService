package com.example.project;

import com.example.project.model.alerts.Alert;
import com.example.project.model.events.Event;
import com.example.project.model.events.EventField;
import com.example.project.model.events.EventType;
import com.example.project.model.rules.AlertRule;
import com.example.project.model.rules.Severity;
import com.example.project.model.rules.conditions.Condition;
import com.example.project.model.rules.conditions.GreaterThanCondition;
import com.example.project.model.rules.conditions.LessThanCondition;
import com.example.project.repository.AlertRepository;
import com.example.project.repository.AlertRuleRepository;
import com.example.project.repository.IAlertRepository;
import com.example.project.repository.IAlertRuleRepository;
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
        IAlertRuleRepository alertRuleRepository = new AlertRuleRepository();
        IAlertRepository alertRepository = new AlertRepository();

        AlertRuleService ruleService = new AlertRuleService(alertRuleRepository);
        AlertProcessingService alertProcessingService = new AlertProcessingService(alertRepository, ruleService, Clock.systemUTC());


        Condition lessThanCondition = new LessThanCondition(11.0);
        Condition greaterThanCondition = new GreaterThanCondition(1.0);
        AlertRule alert = new AlertRule("cpuCheck", EventType.CPU, EventField.CPU_USAGE, Severity.INFO, lessThanCondition);
        alert.setId(1L);
//        AlertRule alert1 = new AlertRule("diskCheck", EventType.DISK,"disk", Severity.CRITICAL, greaterThanCondition);
//        alert1.setId(2L);
        ruleService.addAlertRule(alert);
//        ruleService.addAlertRule(alert1);
        Map<EventField, Object> map = new HashMap<>();
        map.put(EventField.CPU_USAGE, 10.0);
        Map<EventField, Object> map1 = new HashMap<>();
        map1.put(EventField.DISK_FREE, 9.0);
        Event event = new Event(EventType.CPU, LocalDateTime.now(),map);
        event.setId(3L);
        Event event1 = new Event(EventType.DISK, LocalDateTime.now(), map1);
        event1.setId(5L);
        alertProcessingService.process(event);
        alertProcessingService.process(event1);

    }
}
