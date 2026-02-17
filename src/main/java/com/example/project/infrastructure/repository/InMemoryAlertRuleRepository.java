package com.example.project.infrastructure.repository;

import com.example.project.domain.events.Event;
import com.example.project.domain.repository.AlertRuleRepository;
import com.example.project.domain.rules.AlertRule;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryAlertRuleRepository implements AlertRuleRepository {

    private final Map<Long, AlertRule> ruleMap = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public void saveAlertRule(AlertRule rule) {
        if (rule.getId() == null) {
            rule.setId(idGenerator.getAndIncrement());
        }
        ruleMap.put(rule.getId(), rule);
    }

    @Override
    public Optional<AlertRule> getAlertRule(Long id) {
        return Optional.ofNullable(ruleMap.get(id));
    }

    public List<AlertRule> findRuleByEvent(Event event) {
        List<AlertRule> list = new LinkedList<>();
        for (AlertRule alertRule : ruleMap.values()) {
            if (alertRule.isMatchesEventType(event)) {
                list.add(alertRule);
            }
        }
        return list;
    }

    @Override
    public void deleteAlertRule(Long id) {
        if (ruleMap.containsKey(id)) {
            ruleMap.remove(id);
        }
    }
}
