package com.example.RealTimeAlertingService;

import com.example.project.domain.events.*;
import com.example.project.domain.alerts.*;
import com.example.project.domain.rules.conditions.*;
import com.example.project.domain.rules.*;
import com.example.project.exceptions.NotSupportedTypeException;
import com.example.project.infrastructure.InMemoryAlertRepository;
import com.example.project.infrastructure.InMemoryAlertRuleRepository;
import com.example.project.domain.repository.AlertRepository;
import com.example.project.domain.repository.AlertRuleRepository;
import com.example.project.services.AlertProcessingService;
import com.example.project.services.AlertRuleService;
import com.example.project.services.AlertService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.*;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;


public class RealTimeAlertingServiceApplicationTests {

    AlertRuleRepository ruleRepository;
    AlertRepository alertRepository;
    AlertRuleService alertRuleService;

    AlertProcessingService processingService;
    LocalDateTime now;

    Clock fixedClock = Clock.fixed(
            Instant.parse("2026-01-01T10:00:00Z"),
            ZoneOffset.UTC
    );
    Clock otherClock = Clock.systemUTC();

    @BeforeEach
    public void setUp() {
        ruleRepository = new InMemoryAlertRuleRepository();
        alertRepository = new InMemoryAlertRepository();
        alertRuleService = new AlertRuleService(ruleRepository, fixedClock);
        processingService = new AlertProcessingService(alertRepository, alertRuleService, fixedClock);
        now = LocalDateTime.now(fixedClock);
    }

    @Test
    @DisplayName("Создается алерт, если все условия выполнены")
    public void shouldCreateAlertWhenConditionIsMet() {
        Condition greaterThan = new GreaterThanCondition(1.0);
        AlertRule rule = new AlertRule(
                "cpuCheck",
                EventType.CPU,
                EventField.CPU_USAGE,
                Severity.CRITICAL,
                greaterThan, now);
        alertRuleService.addAlertRule(rule);

        Map<EventField, Object> map = new HashMap<>();
        map.put(EventField.CPU_USAGE, 10.0);
        Event event = new Event(EventType.CPU, now, map);

        processingService.process(event);

        assertTrue(alertRepository.findActiveByRule(rule).isPresent());
    }


    @Test
    @DisplayName("Переводит статус алерта в failed после 3 обработок события")
    public void shouldCreateAlertAndRetry() {
        Condition greaterThan = new GreaterThanCondition(10.0);
        AlertRule rule = new AlertRule("cpuLog", EventType.CPU, EventField.CPU_USAGE, Severity.INFO, greaterThan, now);
        alertRuleService.addAlertRule(rule);

        Map<EventField, Object> map = new HashMap<>();
        map.put(EventField.CPU_USAGE, 50.0);
        Event event1 = new Event(EventType.CPU, now, map);


        processingService.process(event1);

        processingService.process(event1);
        processingService.process(event1);
        processingService.process(event1);
//        processingService.process(event1);


        Alert alert = alertRepository.getAlert(1L).get();
        assertEquals(AlertStatus.FAILED, alert.getStatus());
    }


    @Test
    @DisplayName("Алерт не создается, если value Event > condition")
    public void shouldNotCreateAlertWhenConditionIsNotMet() {
        Condition lessThan = new LessThanCondition(10.0);
        AlertRule rule = new AlertRule(
                "check",
                EventType.CPU,
                EventField.CPU_USAGE,
                Severity.INFO,
                lessThan, now
        );
        alertRuleService.addAlertRule(rule);

        Map<EventField, Object> map = new HashMap<>();
        map.put(EventField.CPU_USAGE, 13.0);
        Event event = new Event(EventType.CPU, now, map);

        processingService.process(event);

        assertFalse(alertRepository.findActiveByRule(rule).isPresent());
    }

    @Test
    @DisplayName("Второй алерт не создается, если правило одно и то же")
    public void shouldNotCreateAlertWithSameRule() {
        Condition lessThan = new LessThanCondition(10.0);
        AlertRule rule = new AlertRule(
                "check",
                EventType.CPU,
                EventField.CPU_USAGE,
                Severity.INFO,
                lessThan, LocalDateTime.now(fixedClock)
        );
        alertRuleService.addAlertRule(rule);

        Map<EventField, Object> map = new HashMap<>();
        map.put(EventField.CPU_USAGE, 1.0);
        Event event = new Event(EventType.CPU, LocalDateTime.now(fixedClock), map);
        Event event2 = new Event(EventType.CPU, LocalDateTime.now(fixedClock), map);

        processingService.process(event);
        processingService.process(event2);

        assertEquals(1, alertRepository.getAllAlert().size());
    }


    @Test
    @DisplayName("Выбрасывает исключение если type event не совпадает с field event")
    public void shouldThrowExceptionWhenEventTypeNotEqualsField() {
        Condition lessThan = new LessThanCondition(10.0);
        AlertRule rule = new AlertRule(
                "check",
                EventType.CPU,
                EventField.CPU_USAGE,
                Severity.INFO,
                lessThan, now
        );
        alertRuleService.addAlertRule(rule);

        Map<EventField, Object> map = new HashMap<>();
        map.put(EventField.DISK_FREE, 1.0);
        NotSupportedTypeException e = Assertions.assertThrows(NotSupportedTypeException.class,
                () -> new Event(EventType.CPU, now, map));

        assertTrue(e.getMessage().contains("not allowed for event type"));
    }


    @Test
    @DisplayName("Выбрасывает исключение если type rule не совпадает с field rule")
    public void shouldThrowExceptionWhenTypeRuleNotEqualsField() {
        Condition lessThan = new LessThanCondition(1.0);
        NotSupportedTypeException e = Assertions.assertThrows(NotSupportedTypeException.class,
                () -> new AlertRule("check",
                        EventType.CPU,
                        EventField.DISK_FREE,
                        Severity.INFO,
                        lessThan, now));
        assertTrue(e.getMessage().contains("not supported by"));
    }


    @Test
    @DisplayName("Не создается алерт когда eventType не совпадает")
    public void shouldNotCreateAlertWhenEventTypeNotEquals() {
        Condition lessThan = new LessThanCondition(100.0);
        AlertRule rule = new AlertRule(
                "rule",
                EventType.CPU,
                EventField.CPU_USAGE,
                Severity.INFO,
                lessThan, now
        );
        alertRuleService.addAlertRule(rule);

        Map<EventField, Object> map = new HashMap<>();
        map.put(EventField.DISK_FREE, 5.0);
        Event event = new Event(EventType.DISK, now, map);

        processingService.process(event);

        assertEquals(0, alertRepository.getAllAlert().size());

    }

    @Test
    @DisplayName("Безопасное поведение при отсутствии поля в Event")
    public void shouldBehaveSafely() {
        Condition lessThan = new LessThanCondition(100.0);
        AlertRule rule = new AlertRule(
                "rule",
                EventType.CPU,
                EventField.CPU_USAGE,
                Severity.INFO,
                lessThan, now
        );
        alertRuleService.addAlertRule(rule);

        Map<EventField, Object> map = new HashMap<>();
        map.put(EventField.CPU_USAGE, "12.0");
        Event event = new Event(EventType.CPU, now, map);

        processingService.process(event);
        assertEquals(0, alertRepository.getAllAlert().size());
    }


    @Test
    @DisplayName("Создается алерт по нужному правилу из нескольких")
    public void shouldCreateOneAlertWhenManyRules() {
        Condition lessThan = new LessThanCondition(70.0);
        Condition greaterThan = new GreaterThanCondition(30.0);

        AlertRule rule1 = new AlertRule(
                "rule1",
                EventType.CPU,
                EventField.CPU_USAGE,
                Severity.INFO,
                lessThan, now
        );
        AlertRule rule2 = new AlertRule(
                "rule2",
                EventType.CPU,
                EventField.CPU_USAGE,
                Severity.INFO,
                greaterThan, now
        );
        AlertRule rule3 = new AlertRule(
                "rule3",
                EventType.CPU,
                EventField.CPU_USAGE,
                Severity.INFO,
                lessThan.and(greaterThan), now
        );
        alertRuleService.addAlertRule(rule1);
        alertRuleService.addAlertRule(rule2);
        alertRuleService.addAlertRule(rule3);

        Map<EventField, Object> map = new HashMap<>();
        map.put(EventField.CPU_USAGE, 90.0);
        Event event = new Event(EventType.CPU, now, map);

        processingService.process(event);
        assertEquals(1, alertRepository.getAllAlert().size());
    }

    @Test
    @DisplayName("Не создает алерты когда событие вне comparison window")
    public void shouldNotCreateAlertWhenEventNotInComparisonWindow() {
        Condition lessThan = new LessThanCondition(40.0);
        AlertRuleService.createAlertRule("cpuCheck", EventType.CPU, EventField.CPU_USAGE, Severity.INFO, lessThan);
        Map<EventField, Object> map = new HashMap<>();
        map.put(EventField.CPU_USAGE, 10.0);
        Event event = new Event(EventType.CPU, LocalDateTime.of(2021, 5, 21, 11, 55), map);

        processingService.process(event);

        assertEquals(0, alertRepository.getAllAlert().size());
    }

    @Test
    @DisplayName("Не увеличивает число ретраев когда срабатывает кулдаун")
    public void shouldNotCreateAlertWhenAlertInCooldown() {
        Condition lessThan = new LessThanCondition(60.0);
        AlertRule rule = new AlertRule("cpuCheck", EventType.CPU, EventField.CPU_USAGE, Severity.INFO, lessThan, LocalDateTime.now(fixedClock));
        rule.setCooldownInSeconds(100);
        rule.setComparisonWindow(10000000L);
        alertRuleService.addAlertRule(rule);

        Map<EventField, Object> map = new HashMap<>();
        map.put(EventField.CPU_USAGE, 10.0);
        Event event = new Event(EventType.CPU,
                LocalDateTime.now(fixedClock), map);
        Event event1 = new Event(EventType.CPU,
                LocalDateTime.now(fixedClock), map);

        processingService.process(event);
        processingService.process(event1);

        assertEquals(0, alertRepository.getAllAlert().get(0).getRetryCount());
        assertEquals(1, alertRepository.getAllAlert().size());
    }

    @Test
    @DisplayName("Не создает алерт когда правило выключено")
    public void shouldNotCreateAlertWhenRuleDisabled() {
        Condition lessThan = new LessThanCondition(60.0);
        AlertRule rule = new AlertRule("name", EventType.CPU, EventField.CPU_USAGE, Severity.INFO, lessThan, now);
        rule.setEnabled(false);
        alertRuleService.addAlertRule(rule);

        Map<EventField, Object> map = new HashMap<>();
        map.put(EventField.CPU_USAGE, 40.0);

        Event event = new Event(EventType.CPU, now, map);

        processingService.process(event);

        assertEquals(0, alertRepository.getAllAlert().size());
    }


    @Test
    @DisplayName("Увеличивает число макс ретраев до 5 при Severity.CRITICAL")
    public void shouldIncreaseMaxRetriesWhenRuleSeverityIsCritical() {
        AlertService service = new AlertService(fixedClock);
        AlertRule rule = new AlertRule("criticalRule", EventType.CPU,
                EventField.CPU_USAGE, Severity.CRITICAL,
                new GreaterThanCondition(85.0), now);
        alertRuleService.addAlertRule(rule);

        Map<EventField, Object> map = new HashMap<>();
        map.put(EventField.CPU_USAGE, 90);
        Event event = new Event(EventType.CPU, now, map);

        processingService.process(event);
        service.processEvent(alertRepository.getAlert(1L).get());
        processingService.process(event);
        processingService.process(event);
        processingService.process(event);
        assertEquals(AlertStatus.ACTIVATED, alertRepository.getAlert(1L).get().getStatus());
        processingService.process(event);
        processingService.process(event);

        assertEquals(AlertStatus.FAILED, alertRepository.getAlert(1L).get().getStatus());
    }
}
