package com.practicum.inventory_service.repository;

import com.practicum.inventory_service.domain.Submission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SubmissionRepository extends JpaRepository<Submission, String> {
    List<Submission> findByAssessmentId(String assessmentId);
    Optional<Submission> findByAssessmentIdAndStudentId(String assessmentId, String studentId);
}
