package com.example.project.controller;

import com.example.project.dto.request.AlertRuleRequest;
import com.example.project.dto.request.AlertRuleRequestMapper;
import com.example.project.dto.request.ConditionRequestMapper;
import com.example.project.dto.response.AlertRuleResponse;
import com.example.project.dto.response.AlertRuleResponseMapper;
import com.example.project.services.AlertRuleService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@Tag(name = "rules")
@RestController
@RequestMapping("/rules")
@RequiredArgsConstructor
public class AlertRuleController {

    private final AlertRuleService ruleService;
    private final AlertRuleResponseMapper responseMapper;
    private final AlertRuleRequestMapper requestMapper;
    private final ConditionRequestMapper conditionRequestMapper;

    @GetMapping("/{id}")
    public AlertRuleResponse getAlertRule(@PathVariable Long id) {
        return responseMapper.toAlertRuleResponse(ruleService.getAlertRule(id));
    }
    
    @PostMapping("/create")
    public void createAlertRule(@RequestBody @Valid AlertRuleRequest rule) {
        ruleService.addAlertRule(requestMapper.toDomain(rule, conditionRequestMapper));
    }

    @DeleteMapping("/delete/{id}")
    public void deleteAlertRule(@PathVariable Long id) {
        ruleService.deleteAlertRule(id);
    }
}

