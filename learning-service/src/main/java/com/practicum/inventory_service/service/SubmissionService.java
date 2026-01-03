package com.practicum.inventory_service.service;

import com.practicum.inventory_service.config.KafkaConfig;
import com.practicum.inventory_service.domain.Submission;
import com.practicum.inventory_service.domain.SubmissionStatus;
import com.practicum.inventory_service.dto.GradeRequest;
import com.practicum.inventory_service.dto.SubmissionRequest;
import com.practicum.inventory_service.events.LearningEvent;
import com.practicum.inventory_service.repository.SubmissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SubmissionService {

    private final SubmissionRepository submissionRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final AssessmentService assessmentService;

    @Transactional
    public Submission submit(String assessmentId, String studentId, SubmissionRequest request) {
        submissionRepository.findByAssessmentIdAndStudentId(assessmentId, studentId)
                .ifPresent(sub -> {
                    throw new IllegalStateException("Submission already exists");
                });

        String courseId = assessmentService.findById(assessmentId).getCourseId();

        Submission submission = submissionRepository.save(Submission.builder()
                .assessmentId(assessmentId)
                .studentId(studentId)
                .submissionLink(request.getSubmissionLink())
                .status(SubmissionStatus.SUBMITTED)
                .build());

        kafkaTemplate.send(KafkaConfig.LEARNING_TOPIC,
                new LearningEvent(studentId, courseId, "SUBMISSION_CREATED", "assessment:" + assessmentId));
        return submission;
    }

    @Transactional
    public Submission grade(String submissionId, GradeRequest request, String graderRole) {
        if (!"TEACHER".equals(graderRole) && !"ADMIN".equals(graderRole)) {
            throw new IllegalArgumentException("Only teachers/admins can grade");
        }
        Submission submission = submissionRepository.findById(submissionId)
                .orElseThrow(() -> new IllegalArgumentException("Submission not found"));
        submission.setStatus(SubmissionStatus.GRADED);
        submission.setScore(request.getScore());
        submission.setFeedback(request.getFeedback());
        submission.setGradedAt(Instant.now());

        String courseId = assessmentService.findById(submission.getAssessmentId()).getCourseId();
        kafkaTemplate.send(KafkaConfig.LEARNING_TOPIC,
                new LearningEvent(submission.getStudentId(), courseId, "SUBMISSION_GRADED", "score:" + request.getScore()));
        return submissionRepository.save(submission);
    }

    public List<Submission> byAssessment(String assessmentId) {
        return submissionRepository.findByAssessmentId(assessmentId);
    }
}
