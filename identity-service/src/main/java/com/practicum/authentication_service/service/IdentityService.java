package com.practicum.authentication_service.service;

import com.practicum.authentication_service.config.KafkaConfig;
import com.practicum.authentication_service.domain.Account;
import com.practicum.authentication_service.domain.AccountStatus;
import com.practicum.authentication_service.domain.RoleType;
import com.practicum.authentication_service.dto.AuthResponse;
import com.practicum.authentication_service.dto.LoginRequest;
import com.practicum.authentication_service.dto.RegisterRequest;
import com.practicum.authentication_service.dto.RoleUpdateRequest;
import com.practicum.authentication_service.events.AccountRegisteredEvent;
import com.practicum.authentication_service.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class IdentityService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final AuthenticationManager authenticationManager;

    private static final Duration ACCESS_TOKEN_TTL = Duration.ofHours(6);

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (accountRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Account already exists for email");
        }
        RoleType role = request.getRole() == null ? RoleType.STUDENT : request.getRole();

        Account account = accountRepository.save(Account.builder()
                .email(request.getEmail().toLowerCase())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(role)
                .status(AccountStatus.ACTIVE)
                .displayName(request.getDisplayName())
                .build());

        kafkaTemplate.send(KafkaConfig.ACCOUNT_TOPIC,
                new AccountRegisteredEvent(account.getId(), account.getEmail(), account.getRole(), account.getDisplayName()));

        String token = jwtService.generateToken(account, ACCESS_TOKEN_TTL);
        return AuthResponse.builder()
                .accessToken(token)
                .expiresIn(ACCESS_TOKEN_TTL.toSeconds())
                .role(account.getRole().name())
                .accountId(account.getId())
                .build();
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        Account account = accountRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));

        String token = jwtService.generateToken(account, ACCESS_TOKEN_TTL);
        return AuthResponse.builder()
                .accessToken(token)
                .expiresIn(ACCESS_TOKEN_TTL.toSeconds())
                .role(account.getRole().name())
                .accountId(account.getId())
                .build();
    }

    @Transactional
    public Account updateRole(RoleUpdateRequest request) {
        Account account = accountRepository.findById(request.getAccountId())
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));
        account.setRole(request.getRole());
        return accountRepository.save(account);
    }
}
