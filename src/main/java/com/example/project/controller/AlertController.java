package com.example.project.controller;

import com.example.project.domain.alerts.Alert;
import com.example.project.services.AlertService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/alerts")
@RequiredArgsConstructor
public class AlertController {

    private final AlertService alertService;

    @GetMapping("/all")
    public List<Alert> getAllAlerts(){
        return alertService.getAllAlert();
    }

    @GetMapping("/{id}")
    public Alert getAlert(@PathVariable Long id){
        return alertService.getAlert(id);
    }

}
