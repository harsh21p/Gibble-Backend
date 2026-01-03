package com.practicum.inventory_service.repository;

import com.practicum.inventory_service.domain.Assessment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AssessmentRepository extends JpaRepository<Assessment, String> {
    List<Assessment> findByCourseId(String courseId);
}
