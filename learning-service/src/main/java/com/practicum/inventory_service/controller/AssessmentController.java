package com.practicum.inventory_service.controller;

import com.practicum.inventory_service.domain.Assessment;
import com.practicum.inventory_service.dto.ApiResponse;
import com.practicum.inventory_service.dto.AssessmentRequest;
import com.practicum.inventory_service.service.AssessmentService;
import com.practicum.inventory_service.service.JwtService;
import io.jsonwebtoken.Claims;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/learning")
@RequiredArgsConstructor
public class AssessmentController {

    private final AssessmentService assessmentService;
    private final JwtService jwtService;

    @PostMapping("/courses/{courseId}/assessments")
    public ResponseEntity<ApiResponse<Assessment>> create(@PathVariable String courseId,
                                                          @Valid @RequestBody AssessmentRequest request,
                                                          @RequestHeader("Authorization") String authorization) {
        Claims claims = jwtService.extractAllClaims(authorization.replace("Bearer ", ""));
        String role = claims.get("role", String.class);
        if (!"TEACHER".equals(role) && !"ADMIN".equals(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiResponse<>(403, true, "Only teachers and admins can create assessments", null));
        }
        Assessment assessment = assessmentService.create(courseId, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(201, false, "Assessment created", assessment));
    }

    @GetMapping("/courses/{courseId}/assessments")
    public ResponseEntity<ApiResponse<List<Assessment>>> list(@PathVariable String courseId) {
        List<Assessment> assessments = assessmentService.listByCourse(courseId);
        return ResponseEntity.ok(new ApiResponse<>(200, false, "Assessments", assessments));
    }
}
