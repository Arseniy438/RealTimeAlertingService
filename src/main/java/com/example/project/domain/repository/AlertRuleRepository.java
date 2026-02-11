package com.example.project.domain.repository;

import com.example.project.domain.events.Event;
import com.example.project.domain.rules.AlertRule;

import java.util.List;
import java.util.Optional;

public interface AlertRuleRepository {


    void saveAlertRule(AlertRule rule);
    Optional<AlertRule> getAlertRule(Long id);
    void deleteAlertRule(Long id);
    List<AlertRule> findRuleByEvent(Event event);


}
