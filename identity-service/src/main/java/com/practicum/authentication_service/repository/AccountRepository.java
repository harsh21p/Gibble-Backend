package com.practicum.authentication_service.repository;

import com.practicum.authentication_service.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, String> {
    Optional<Account> findByEmail(String email);
    boolean existsByEmail(String email);
}
