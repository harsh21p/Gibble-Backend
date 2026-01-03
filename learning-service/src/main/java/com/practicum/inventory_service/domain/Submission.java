package com.practicum.inventory_service.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "submissions")
public class Submission {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "assessment_id", nullable = false)
    private String assessmentId;

    @Column(name = "student_id", nullable = false)
    private String studentId;

    @Column(name = "submission_link", length = 512)
    private String submissionLink;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 24)
    private SubmissionStatus status;

    private Integer score;

    @Column(length = 512)
    private String feedback;

    @CreationTimestamp
    @Column(name = "submitted_at", updatable = false)
    private Instant submittedAt;

    @Column(name = "graded_at")
    private Instant gradedAt;
}
