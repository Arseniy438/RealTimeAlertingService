package com.example.project.services;

import com.example.project.infrastructure.persistence.ProfileEntity;
import com.example.project.domain.repository.ProfileRepository;
import com.example.project.dto.request.ProfileRequestDto;
import com.example.project.dto.response.ProfileResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProfileService implements UserDetailsService {

    private final ProfileRepository profileRepository;
    private final MailService mailService;
    private final PasswordEncoder passwordEncoder;


    public ProfileResponseDto registerProfile(ProfileRequestDto requestDto) {
        ProfileEntity newProfile = toEntity(requestDto);
        String code = UUID.randomUUID().toString();
        newProfile.setActivationToken(code);
        ProfileEntity saved = profileRepository.save(newProfile);

        mailService.send(saved.getEmail(), "Authentication link", "http://localhost:8080/activate/" + code);

        return toDto(saved);
    }

    public boolean activateUser(String code) {
        Optional<ProfileEntity> profile = profileRepository.findProfileEntityByActivationToken(code);

        if (profile.isEmpty()) {
            return false;
        }
        profile.get().setActivationToken(null);
        profile.get().setIsActive(true);
        profileRepository.save(profile.get());
        return true;
    }

    private ProfileEntity toEntity(ProfileRequestDto dto) {
        return ProfileEntity.builder()
                .fullName(dto.getFullName())
                .email(dto.getEmail())
                .hashPassword(passwordEncoder.encode(dto.getPassword()))
                .build();
    }

    private ProfileResponseDto toDto(ProfileEntity entity) {
        return ProfileResponseDto.builder()
                .id(entity.getId())
                .fullName(entity.getFullName())
                .email(entity.getEmail())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return profileRepository.findProfileByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
