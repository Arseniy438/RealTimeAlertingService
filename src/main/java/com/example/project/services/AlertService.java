package com.example.project.services;

import com.example.project.domain.alerts.Alert;
import com.example.project.domain.events.EventType;
import com.example.project.domain.rules.Severity;
import java.time.Clock;
import java.time.LocalDateTime;

public class AlertService {

    private final Clock clock;

    public AlertService(Clock clock) {
        this.clock = clock;
    }

    public void processEvent(Alert alert){
        LocalDateTime now = LocalDateTime.now(clock);
//        AlertStatus.NEW
//        AlertStatus.ACTIVATED
//        AlertStatus.FAILED
//        AlertStatus.ACKNOWLEDGED
//        AlertStatus.RESOLVED

        if(alert.getRule().getSeverity() == Severity.CRITICAL){
            alert.getRule().setMaxRetries(5);
//            alert.activate(now);
        }

        if(!alert.canRetry(now)){
            System.out.println("Status: "+alert.getStatus() +
                    "\nCooldown: " + alert.getRule().getCooldownInSeconds()+
                    "\nRetries: " + alert.getRetryCount() + " MAX="+alert.getRule().getMaxRetries());
        }

        if(alert.getEvent().getType() == EventType.RECOVERY){
            alert.resolve(now);
        }








    }
}
