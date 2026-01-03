package com.practicum.user_service.repository;

import com.practicum.user_service.domain.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserProfileRepository extends JpaRepository<UserProfile, String> {
    Optional<UserProfile> findByAccountId(String accountId);
    boolean existsByEmail(String email);
}
