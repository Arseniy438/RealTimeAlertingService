package com.example.RealTimeAlertingService;

import com.example.project.RealTimeAlertingServiceApplication;
import com.example.project.domain.alerts.AlertStatus;
import com.example.project.domain.events.Event;
import com.example.project.domain.events.EventField;
import com.example.project.domain.events.EventType;
import com.example.project.domain.rules.Severity;
import com.example.project.domain.rules.conditions.ConditionType;
import com.example.project.infrastructure.persistence.AlertEntity;
import com.example.project.infrastructure.persistence.AlertRuleEntity;
import com.example.project.infrastructure.persistence.EventEntity;
import com.example.project.infrastructure.persistence.jpa.AlertEntityRepository;
import com.example.project.infrastructure.persistence.jpa.AlertRuleEntityRepository;
import com.example.project.infrastructure.persistence.jpa.EventEntityRepository;
import com.example.project.infrastructure.persistence.mapper.EventMapper;
import com.example.project.services.AlertProcessingService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.Clock;
import java.time.Instant;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@Import(FixedClockConfig.class)
@SpringBootTest(classes = RealTimeAlertingServiceApplication.class)
@Testcontainers
public class PostgresTestcontainersIntegrationTest {
    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine")
            .withDatabaseName("alerts_test")
            .withUsername("alerts")
            .withPassword("alerts");

    @DynamicPropertySource
    static void configureDataSource(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.datasource.driver-class-name", postgres::getDriverClassName);

        registry.add("spring.jpa.hibernate.ddl-auto", () -> "none");
        registry.add("spring.jpa.show-sql", () -> "false");
    }

    @BeforeEach
    void cleanDb() {
        alertEntityRepository.deleteAll();
        eventEntityRepository.deleteAll();
        alertRuleEntityRepository.deleteAll();
    }

    @Autowired
    private AlertRuleEntityRepository alertRuleEntityRepository;

    @Autowired
    private AlertEntityRepository alertEntityRepository;

    @Autowired
    AlertProcessingService alertProcessingService;

    @Autowired
    private EventEntityRepository eventEntityRepository;

    @Autowired
    private EventMapper eventMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private Clock clock;


    @Test
    @DisplayName("Сохраняет правило в бд")
    void shouldPersistRuleInRealPostgresContainer() {
        AlertRuleEntity cpuRule = new AlertRuleEntity(
                "cpu-above-90",
                true,
                EventType.CPU,
                EventField.CPU_USAGE,
                Severity.CRITICAL,
                ConditionType.GREATER_THAN,
                90.0,
                "CPU usage is too high",
                60,
                5,
                300,
                clock.instant()
        );

        alertRuleEntityRepository.save(cpuRule);

        List<AlertRuleEntity> criticalRules = alertRuleEntityRepository.findAllBySeverity(Severity.CRITICAL);
        assertEquals(1, criticalRules.size());
        assertEquals("cpu-above-90", criticalRules.get(0).getName());
    }

    @Test
    @DisplayName("Сохраняет событие в бд")
    void shouldPersistEventInContainer() {
        EventEntity eventCpu = new EventEntity(EventType.CPU, clock.instant(), "RESPONSE_TIME");
        eventEntityRepository.save(eventCpu);

        List<EventEntity> events = eventEntityRepository.findAll();
        assertEquals(1, events.size());
        assertEquals(EventType.CPU.name(), events.get(0).getType().name());
    }

    @Test
    @DisplayName("Создает алерт и сохраняет в бд")
    void shouldCreateAlertForEvent() throws JsonProcessingException {
        Map<String, Object> payload = Map.of(
                EventField.DISK_FREE.name(), 95.0
        );
        AlertRuleEntity diskRule = new AlertRuleEntity(
                "disk-above-90",
                true,
                EventType.DISK,
                EventField.DISK_FREE,
                Severity.CRITICAL,
                ConditionType.GREATER_THAN,
                90.0,
                "DISK usage is too high",
                60,
                5,
                300,
                clock.instant());

        alertRuleEntityRepository.save(diskRule);

        EventEntity diskEvent = new EventEntity(EventType.DISK, clock.instant(), objectMapper.writeValueAsString(payload));
        eventEntityRepository.save(diskEvent);

        alertProcessingService.process(eventMapper.toDomain(diskEvent));

        List<AlertEntity> alerts = alertEntityRepository.findAll();
        assertEquals(1, alerts.size());

        AlertEntity alert = alerts.get(0);
        assertEquals(Severity.CRITICAL, alert.getSeverity());
        assertEquals("DISK usage is too high", alert.getMessage());
        assertEquals(diskRule.getId(), alert.getRule().getId());
    }

    @Test
    @DisplayName("Не создает алерт")
    void shouldNotCreateAlert() throws JsonProcessingException {
        Map<String, Object> payload = Map.of(
                EventField.CPU_USAGE.name(), 10.0
        );
        AlertRuleEntity diskRule = new AlertRuleEntity(
                "disk-above-90",
                true,
                EventType.DISK,
                EventField.DISK_FREE,
                Severity.CRITICAL,
                ConditionType.GREATER_THAN,
                90.0,
                "DISK usage is too high",
                60,
                5,
                300,
                clock.instant());

        alertRuleEntityRepository.save(diskRule);

        EventEntity diskEvent = new EventEntity(EventType.CPU, clock.instant(), objectMapper.writeValueAsString(payload));
        eventEntityRepository.save(diskEvent);

        alertProcessingService.process(eventMapper.toDomain(diskEvent));

        List<AlertEntity> alerts = alertEntityRepository.findAll();
        assertEquals(0, alerts.size());
    }

    @Test
    @DisplayName("Второй алерт не создается, если правило одно и то же")
    void shouldNotCreateAlertWithSameRule() throws JsonProcessingException {
        Map<String, Object> payload = Map.of(
                EventField.DISK_FREE.name(), 95.0
        );
        AlertRuleEntity diskRule = new AlertRuleEntity(
                "disk-above-90",
                true,
                EventType.DISK,
                EventField.DISK_FREE,
                Severity.CRITICAL,
                ConditionType.GREATER_THAN,
                90.0,
                "DISK usage is too high",
                60,
                5,
                300,
                clock.instant());

        alertRuleEntityRepository.save(diskRule);

        EventEntity diskEvent = new EventEntity(EventType.DISK, clock.instant(), objectMapper.writeValueAsString(payload));
        EventEntity diskEvent2 = new EventEntity(EventType.DISK, clock.instant(), objectMapper.writeValueAsString(payload));
        eventEntityRepository.save(diskEvent);
        eventEntityRepository.save(diskEvent2);

        alertProcessingService.process(eventMapper.toDomain(diskEvent));
        alertProcessingService.process(eventMapper.toDomain(diskEvent2));

        List<AlertEntity> alerts = alertEntityRepository.findAll();
        assertEquals(1, alerts.size());
        assertEquals(diskRule.getId(), alerts.get(0).getRule().getId());
    }

    @Test
    @DisplayName("Переводит статус алерта в failed после 3 обработок события")
    void shouldChangeStatusToFailedAfterThreeRetries() throws JsonProcessingException {
        Map<String, Object> payload = Map.of(
                EventField.DISK_FREE.name(), 95.0
        );
        AlertRuleEntity diskRule = new AlertRuleEntity(
                "disk-above-90",
                true,
                EventType.DISK,
                EventField.DISK_FREE,
                Severity.INFO,
                ConditionType.GREATER_THAN,
                90.0,
                "DISK usage is too high",
                0,
                3,
                300,
                clock.instant());

        alertRuleEntityRepository.save(diskRule);
        assertEquals(1, alertRuleEntityRepository.findAll().size());

        EventEntity diskEvent = new EventEntity(EventType.DISK, clock.instant(), objectMapper.writeValueAsString(payload));

        eventEntityRepository.save(diskEvent);
        assertEquals(1, eventEntityRepository.findAll().size());

        Event event = eventMapper.toDomain(eventEntityRepository.findAll().get(0));

        alertProcessingService.process(event);
        alertProcessingService.process(event);
        alertProcessingService.process(event);
        alertProcessingService.process(event);

        List<AlertEntity> alerts = alertEntityRepository.findAll();
        assertEquals(1, alerts.size());
        AlertEntity alert = alerts.get(0);
        assertEquals(3, alert.getRetryCount());
        assertEquals(AlertStatus.FAILED, alert.getStatus());

    }

    @Test
    @DisplayName("Не создает алерт когда правило выключено")
    void shouldNotCreateAlertWhenRuleDisabled() throws JsonProcessingException {
        Map<String, Object> payload = Map.of(
                EventField.DISK_FREE.name(), 95.0
        );
        AlertRuleEntity diskRule = new AlertRuleEntity(
                "disk-above-90",
                false,
                EventType.DISK,
                EventField.DISK_FREE,
                Severity.CRITICAL,
                ConditionType.GREATER_THAN,
                90.0,
                "DISK usage is too high",
                60,
                5,
                300,
                clock.instant());

        alertRuleEntityRepository.save(diskRule);

        EventEntity diskEvent = new EventEntity(EventType.DISK, clock.instant(), objectMapper.writeValueAsString(payload));
        eventEntityRepository.save(diskEvent);
        assertEquals(1, eventEntityRepository.findAll().size());

        alertProcessingService.process(eventMapper.toDomain(diskEvent));

        List<AlertEntity> alerts = alertEntityRepository.findAll();
        assertEquals(0, alerts.size());
    }

}

