package com.example.project.domain.repository;

import com.example.project.infrastructure.persistence.ProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfileRepository extends JpaRepository<ProfileEntity, Long> {
    Optional<ProfileEntity> findProfileByEmail(String email);

    Optional<ProfileEntity> findProfileEntityByActivationToken(String activationToken);
}
