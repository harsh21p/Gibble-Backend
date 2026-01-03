package com.practicum.user_service.repository;

import com.practicum.user_service.domain.LearningActivity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LearningActivityRepository extends JpaRepository<LearningActivity, String> {
}
