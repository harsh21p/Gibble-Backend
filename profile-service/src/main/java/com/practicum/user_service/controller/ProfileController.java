package com.practicum.user_service.controller;

import com.practicum.user_service.dto.ApiResponse;
import com.practicum.user_service.dto.ProfileResponse;
import com.practicum.user_service.dto.ProfileUpdateRequest;
import com.practicum.user_service.service.JwtService;
import com.practicum.user_service.service.ProfileService;
import io.jsonwebtoken.Claims;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profiles")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;
    private final JwtService jwtService;

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<ProfileResponse>> me(@RequestHeader("Authorization") String authorization) {
        Claims claims = jwtService.extractAllClaims(authorization.replace("Bearer ", ""));
        ProfileResponse profile = profileService.findByAccountId(claims.getSubject());
        return ResponseEntity.ok(new ApiResponse<>(200, false, "profile", profile));
    }

    @PutMapping("/me")
    public ResponseEntity<ApiResponse<ProfileResponse>> updateMe(@Valid @RequestBody ProfileUpdateRequest request,
                                                                 @RequestHeader("Authorization") String authorization) {
        Claims claims = jwtService.extractAllClaims(authorization.replace("Bearer ", ""));
        request.setAccountId(claims.getSubject());
        request.setEmail(claims.get("email", String.class));
        ProfileResponse profile = profileService.update(request);
        return ResponseEntity.ok(new ApiResponse<>(200, false, "updated", profile));
    }

    @GetMapping("/{accountId}")
    public ResponseEntity<ApiResponse<ProfileResponse>> byAccount(@PathVariable String accountId,
                                                                  @RequestHeader("Authorization") String authorization) {
        Claims claims = jwtService.extractAllClaims(authorization.replace("Bearer ", ""));
        String role = claims.get("role", String.class);
        if (!"ADMIN".equals(role) && !"TEACHER".equals(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiResponse<>(403, true, "Only instructors and admins can view other profiles", null));
        }
        ProfileResponse profile = profileService.findByAccountId(accountId);
        return ResponseEntity.ok(new ApiResponse<>(200, false, "profile", profile));
    }
}
