package com.example.project.repository;

import com.example.project.model.events.Event;
import com.example.project.model.rules.AlertRule;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public interface IAlertRuleRepository {


    void addAlertRule(AlertRule rule);
    Optional<AlertRule> getAlertRule(Long id);
    void deleteAlertRule(Long id);
    List<AlertRule> findRuleByEvent(Event event);


}
