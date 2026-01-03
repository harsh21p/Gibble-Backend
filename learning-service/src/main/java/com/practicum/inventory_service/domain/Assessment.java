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
@Table(name = "assessments")
public class Assessment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "course_id", nullable = false)
    private String courseId;

    @Column(nullable = false, length = 180)
    private String title;

    @Column(length = 512)
    private String instructions;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 24)
    private AssessmentType type;

    @Column(name = "due_date")
    private Instant dueDate;

    @Column(name = "max_score")
    private Integer maxScore;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Instant createdAt;
}
