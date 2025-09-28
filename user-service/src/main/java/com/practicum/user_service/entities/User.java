package com.practicum.user_service.entities;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.ZonedDateTime;

@Data
@Entity(name = "user_details")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(unique = true , nullable = false)
    private String userId;

    @Column(unique = true , nullable = false)
    private String email;

    @Column(nullable = false , name = "first_name")
    private String firstName;

    private String lastName;

    private String address;

    private String username;

    @Column(nullable = false , name = "created_at")
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private ZonedDateTime createdAt = ZonedDateTime.now();

}
