package com.practicum.inventory_service.controller;

import com.practicum.inventory_service.domain.Submission;
import com.practicum.inventory_service.dto.ApiResponse;
import com.practicum.inventory_service.dto.GradeRequest;
import com.practicum.inventory_service.dto.SubmissionRequest;
import com.practicum.inventory_service.service.JwtService;
import com.practicum.inventory_service.service.SubmissionService;
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
public class SubmissionController {

    private final SubmissionService submissionService;
    private final JwtService jwtService;

    @PostMapping("/assessments/{assessmentId}/submissions")
    public ResponseEntity<ApiResponse<Submission>> submit(@PathVariable String assessmentId,
                                                          @Valid @RequestBody SubmissionRequest request,
                                                          @RequestHeader("Authorization") String authorization) {
        Claims claims = jwtService.extractAllClaims(authorization.replace("Bearer ", ""));
        String role = claims.get("role", String.class);
        if (!"STUDENT".equals(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiResponse<>(403, true, "Only students can submit work", null));
        }
        Submission submission = submissionService.submit(assessmentId, claims.getSubject(), request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(201, false, "Submitted", submission));
    }

    @GetMapping("/assessments/{assessmentId}/submissions")
    public ResponseEntity<ApiResponse<List<Submission>>> byAssessment(@PathVariable String assessmentId,
                                                                      @RequestHeader("Authorization") String authorization) {
        Claims claims = jwtService.extractAllClaims(authorization.replace("Bearer ", ""));
        String role = claims.get("role", String.class);
        if (!"TEACHER".equals(role) && !"ADMIN".equals(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiResponse<>(403, true, "Only instructors can view submissions", null));
        }
        List<Submission> submissions = submissionService.byAssessment(assessmentId);
        return ResponseEntity.ok(new ApiResponse<>(200, false, "Submissions", submissions));
    }

    @PatchMapping("/submissions/{submissionId}/grade")
    public ResponseEntity<ApiResponse<Submission>> grade(@PathVariable String submissionId,
                                                         @Valid @RequestBody GradeRequest request,
                                                         @RequestHeader("Authorization") String authorization) {
        Claims claims = jwtService.extractAllClaims(authorization.replace("Bearer ", ""));
        Submission submission = submissionService.grade(submissionId, request, claims.get("role", String.class));
        return ResponseEntity.ok(new ApiResponse<>(200, false, "Graded", submission));
    }
}
