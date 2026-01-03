package com.practicum.inventory_service.service;

import com.practicum.inventory_service.domain.Enrollment;
import com.practicum.inventory_service.domain.EnrollmentStatus;
import com.practicum.inventory_service.repository.EnrollmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;

    @Transactional
    public Enrollment enroll(String courseId, String studentId) {
        return enrollmentRepository.findByCourseIdAndStudentId(courseId, studentId)
                .orElseGet(() -> enrollmentRepository.save(Enrollment.builder()
                        .courseId(courseId)
                        .studentId(studentId)
                        .status(EnrollmentStatus.ENROLLED)
                        .build()));
    }

    public List<Enrollment> findByStudent(String studentId) {
        return enrollmentRepository.findByStudentId(studentId);
    }

    public long countActive(String courseId) {
        return enrollmentRepository.countByCourseIdAndStatus(courseId, EnrollmentStatus.ENROLLED);
    }
}
