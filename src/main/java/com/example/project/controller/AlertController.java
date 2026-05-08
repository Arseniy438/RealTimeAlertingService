package com.example.project.controller;

import com.example.project.domain.alerts.Alert;
import com.example.project.dto.response.AlertResponse;
import com.example.project.dto.response.AlertResponseMapper;
import com.example.project.services.AlertService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "alerts")
@RestController
@RequestMapping("/alerts")
@RequiredArgsConstructor
public class AlertController {

    private final AlertService alertService;
    private final AlertResponseMapper alertResponseMapper;

    @GetMapping("/all")
    public List<Alert> getAllAlerts(){
        return alertService.getAllAlert();
    }

    @GetMapping("/{id}")
    public AlertResponse getAlert(@PathVariable Long id){
        return alertResponseMapper.toAlertResponse(alertService.getAlert(id));
    }

    @DeleteMapping("/delete/{id}")
    public void deleteAlert(@PathVariable Long id){
        alertService.deleteAlert(id);
    }

}
