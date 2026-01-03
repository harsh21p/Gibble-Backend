package com.practicum.inventory_service.service;

import com.practicum.inventory_service.domain.Assessment;
import com.practicum.inventory_service.dto.AssessmentRequest;
import com.practicum.inventory_service.repository.AssessmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AssessmentService {

    private final AssessmentRepository assessmentRepository;

    @Transactional
    public Assessment create(String courseId, AssessmentRequest request) {
        Assessment assessment = Assessment.builder()
                .courseId(courseId)
                .title(request.getTitle())
                .instructions(request.getInstructions())
                .type(request.getType())
                .dueDate(request.getDueDate())
                .maxScore(request.getMaxScore())
                .build();
        return assessmentRepository.save(assessment);
    }

    public List<Assessment> listByCourse(String courseId) {
        return assessmentRepository.findByCourseId(courseId);
    }

    public Assessment findById(String id) {
        return assessmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Assessment not found"));
    }
}
