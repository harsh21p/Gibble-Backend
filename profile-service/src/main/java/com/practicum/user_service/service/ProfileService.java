package com.practicum.user_service.service;

import com.practicum.user_service.domain.UserProfile;
import com.practicum.user_service.dto.ProfileResponse;
import com.practicum.user_service.dto.ProfileUpdateRequest;
import com.practicum.user_service.events.AccountRegisteredEvent;
import com.practicum.user_service.repository.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final UserProfileRepository profileRepository;

    @Cacheable(cacheNames = "profiles", key = "#accountId")
    public ProfileResponse findByAccountId(String accountId) {
        return profileRepository.findByAccountId(accountId)
                .map(this::toResponse)
                .orElseThrow(() -> new IllegalArgumentException("Profile not found"));
    }

    @Transactional
    @CacheEvict(cacheNames = "profiles", key = "#request.accountId")
    public ProfileResponse update(ProfileUpdateRequest request) {
        UserProfile profile = profileRepository.findByAccountId(request.getAccountId())
                .orElseThrow(() -> new IllegalArgumentException("Profile not found"));
        profile.setEmail(request.getEmail().toLowerCase());
        profile.setFirstName(request.getFirstName());
        profile.setLastName(request.getLastName());
        profile.setHeadline(request.getHeadline());
        profile.setBio(request.getBio());
        profile.setTimezone(request.getTimezone());
        if (request.getPreferences() != null) {
            profile.getPreferences().putAll(request.getPreferences());
        }
        return toResponse(profileRepository.save(profile));
    }

    @Transactional
    public ProfileResponse ensureProfile(AccountRegisteredEvent event) {
        UserProfile profile = profileRepository.findByAccountId(event.accountId())
                .orElse(UserProfile.builder()
                        .accountId(event.accountId())
                        .email(event.email())
                        .role(event.role())
                        .firstName(event.displayName())
                        .build());
        return toResponse(profileRepository.save(profile));
    }

    private ProfileResponse toResponse(UserProfile profile) {
        return ProfileResponse.builder()
                .accountId(profile.getAccountId())
                .email(profile.getEmail())
                .firstName(profile.getFirstName())
                .lastName(profile.getLastName())
                .headline(profile.getHeadline())
                .bio(profile.getBio())
                .timezone(profile.getTimezone())
                .role(profile.getRole())
                .preferences(profile.getPreferences())
                .build();
    }
}
