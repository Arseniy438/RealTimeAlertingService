package com.example.project.dto.request;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ProfileRequestDto {
    private String fullName;
    private String email;
    private String password;
}
