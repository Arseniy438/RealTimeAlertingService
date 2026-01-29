package com.example.project.services;

import com.example.project.model.events.Event;
import com.example.project.model.rules.AlertRule;
import com.example.project.repository.AlertRuleRepository;
import com.example.project.repository.IAlertRuleRepository;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class AlertRuleService {

    private IAlertRuleRepository alertRuleRepository;

    public AlertRuleService(IAlertRuleRepository alertRuleRepository) {
        this.alertRuleRepository = alertRuleRepository;
    }

    public List<AlertRule> findMatching(Event event){
        return alertRuleRepository.findRuleByEvent(event);
    }


    public void addAlertRule(AlertRule alertRule) {
        alertRuleRepository.addAlertRule(alertRule);
    }

    public Optional<AlertRule> getAlertRule(Long id) {
        return alertRuleRepository.getAlertRule(id);
    }

    public void deleteAlertRule(Long id) {
        alertRuleRepository.deleteAlertRule(id);
    }

}
