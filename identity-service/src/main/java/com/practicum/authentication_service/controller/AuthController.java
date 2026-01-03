package com.practicum.authentication_service.controller;

import com.practicum.authentication_service.domain.RoleType;
import com.practicum.authentication_service.dto.ApiResponse;
import com.practicum.authentication_service.dto.AuthResponse;
import com.practicum.authentication_service.dto.LoginRequest;
import com.practicum.authentication_service.dto.RegisterRequest;
import com.practicum.authentication_service.dto.TokenValidationRequest;
import com.practicum.authentication_service.service.IdentityService;
import com.practicum.authentication_service.service.JwtService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/identity/auth")
@RequiredArgsConstructor
public class AuthController {

    private final IdentityService identityService;
    private final JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponse>> register(@Valid @RequestBody RegisterRequest request) {
        AuthResponse response = identityService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(201, false, "Account created as " + response.getRole(), response));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = identityService.login(request);
        return ResponseEntity.ok(new ApiResponse<>(200, false, "Authenticated", response));
    }

    @PostMapping("/validate")
    public ResponseEntity<ApiResponse<Boolean>> validate(@Valid @RequestBody TokenValidationRequest token) {
        boolean valid = jwtService.isTokenValid(token.getToken());
        return ResponseEntity.ok(new ApiResponse<>(200, false, valid ? "valid" : "invalid", valid));
    }

    @GetMapping("/roles")
    public ResponseEntity<ApiResponse<RoleType[]>> roles() {
        return ResponseEntity.ok(new ApiResponse<>(200, false, "roles", RoleType.values()));
    }
}
