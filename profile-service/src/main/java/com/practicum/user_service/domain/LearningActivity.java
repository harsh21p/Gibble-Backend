package com.practicum.user_service.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "learning_activity")
public class LearningActivity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String accountId;

    @Column(nullable = false)
    private String courseId;

    @Column(nullable = false, length = 64)
    private String eventType;

    @Column(length = 512)
    private String details;

    @CreationTimestamp
    @Column(name = "recorded_at", updatable = false)
    private Instant recordedAt;
}
