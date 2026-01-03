package com.practicum.inventory_service.repository;

import com.practicum.inventory_service.domain.Enrollment;
import com.practicum.inventory_service.domain.EnrollmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EnrollmentRepository extends JpaRepository<Enrollment, String> {
    Optional<Enrollment> findByCourseIdAndStudentId(String courseId, String studentId);
    List<Enrollment> findByStudentId(String studentId);
    List<Enrollment> findByCourseId(String courseId);
    long countByCourseIdAndStatus(String courseId, EnrollmentStatus status);
}
