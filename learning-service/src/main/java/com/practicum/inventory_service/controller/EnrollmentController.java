package com.practicum.inventory_service.controller;

import com.practicum.inventory_service.domain.Enrollment;
import com.practicum.inventory_service.dto.ApiResponse;
import com.practicum.inventory_service.service.EnrollmentService;
import com.practicum.inventory_service.service.JwtService;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/learning/enrollments")
@RequiredArgsConstructor
public class EnrollmentController {

    private final EnrollmentService enrollmentService;
    private final JwtService jwtService;

    @PostMapping("/{courseId}")
    public ResponseEntity<ApiResponse<Enrollment>> enroll(@PathVariable String courseId,
                                                          @RequestHeader("Authorization") String authorization) {
        Claims claims = jwtService.extractAllClaims(authorization.replace("Bearer ", ""));
        String role = claims.get("role", String.class);
        if (!"STUDENT".equals(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiResponse<>(403, true, "Only students can enroll", null));
        }
        Enrollment enrollment = enrollmentService.enroll(courseId, claims.getSubject());
        return ResponseEntity.ok(new ApiResponse<>(200, false, "Enrolled", enrollment));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<List<Enrollment>>> myEnrollments(@RequestHeader("Authorization") String authorization) {
        Claims claims = jwtService.extractAllClaims(authorization.replace("Bearer ", ""));
        List<Enrollment> enrollments = enrollmentService.findByStudent(claims.getSubject());
        return ResponseEntity.ok(new ApiResponse<>(200, false, "Enrollments", enrollments));
    }
}
