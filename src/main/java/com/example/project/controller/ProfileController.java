package com.example.project.controller;

import com.example.project.dto.request.ProfileRequestDto;
import com.example.project.dto.response.ProfileResponseDto;
import com.example.project.services.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/activate/{code}")
    public ResponseEntity<?> activateUser(@PathVariable String code){
        return ResponseEntity.ok(profileService.activateUser(code));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody ProfileRequestDto requestDto) {

        ProfileResponseDto responseDto = profileService.registerProfile(requestDto);

        return ResponseEntity.ok(responseDto);
    }

}
