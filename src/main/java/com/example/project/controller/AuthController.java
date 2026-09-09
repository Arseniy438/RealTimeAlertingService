package com.example.project.controller;

import com.example.project.dto.request.ProfileRequestDto;
import com.example.project.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/sign-in")
    public String signIn(@RequestBody ProfileRequestDto dto){
        return authService.signIn(dto);
    }

    @GetMapping("/health")
    public String healthCheck(){
        return "Hi, health normal";
    }

}
