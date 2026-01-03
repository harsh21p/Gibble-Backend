package com.practicum.inventory_service.repository;

import com.practicum.inventory_service.domain.Course;
import com.practicum.inventory_service.domain.CourseStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, String> {
    Optional<Course> findByCode(String code);
    List<Course> findByStatus(CourseStatus status);
    List<Course> findByTeacherId(String teacherId);
}
