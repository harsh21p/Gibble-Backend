package com.practicum.user_service.repository;

import com.practicum.user_service.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    User save(User user);
    User findUserById(String id);
    User findByEmail(String email);
    void deleteById(String id);
    Optional<User> findUserByUserId(String userId);
}