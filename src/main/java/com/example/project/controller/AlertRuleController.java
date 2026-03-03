package com.example.project.controller;

import com.example.project.domain.rules.AlertRule;
import com.example.project.services.AlertRuleService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("/rules")
@RequiredArgsConstructor
public class AlertRuleController {

    private final AlertRuleService ruleService;

    @GetMapping("/{id}")
    public AlertRule getAlertRule(@PathVariable Long id){
        return ruleService.getAlertRule(id).orElseThrow(NoSuchElementException::new);
    }


    @PostMapping
    public void createAlertRule(){

    }



}

