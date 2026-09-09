package com.example.project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
@EnableScheduling
@SpringBootApplication
public class RealTimeAlertingServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(RealTimeAlertingServiceApplication.class, args);
    }
}
