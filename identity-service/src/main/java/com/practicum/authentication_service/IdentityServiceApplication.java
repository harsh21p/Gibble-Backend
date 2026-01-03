package com.practicum.authentication_service;

import com.practicum.authentication_service.domain.Account;
import com.practicum.authentication_service.domain.AccountStatus;
import com.practicum.authentication_service.domain.RoleType;
import com.practicum.authentication_service.repository.AccountRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@EnableDiscoveryClient
public class IdentityServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(IdentityServiceApplication.class, args);
    }

    @Bean
    CommandLineRunner seedAdmin(AccountRepository repository, PasswordEncoder passwordEncoder) {
        return args -> {
            repository.findByEmail("admin@learning.local").ifPresentOrElse(
                    existing -> {},
                    () -> repository.save(Account.builder()
                            .email("admin@learning.local")
                            .password(passwordEncoder.encode("Admin@123"))
                            .role(RoleType.ADMIN)
                            .status(AccountStatus.ACTIVE)
                            .displayName("Platform Admin")
                            .build())
            );
        };
    }
}
