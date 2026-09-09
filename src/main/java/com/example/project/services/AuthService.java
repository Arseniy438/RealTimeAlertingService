package com.example.project.services;

import com.example.project.dto.request.ProfileRequestDto;
import com.example.project.infrastructure.utils.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager manager;
    private final ProfileService userService;
    private final JwtService jwtUtils;

    public String signIn(ProfileRequestDto request) {
        manager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getEmail(),
                request.getPassword()
        ));

        var user = userService
                .loadUserByUsername(request.getEmail());

        return jwtUtils.generateToken(user);
    }


}



