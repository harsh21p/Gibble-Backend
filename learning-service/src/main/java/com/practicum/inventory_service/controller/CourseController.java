package com.practicum.inventory_service.controller;

import com.practicum.inventory_service.dto.ApiResponse;
import com.practicum.inventory_service.dto.CourseRequest;
import com.practicum.inventory_service.dto.CourseResponse;
import com.practicum.inventory_service.service.CourseService;
import com.practicum.inventory_service.service.JwtService;
import io.jsonwebtoken.Claims;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/learning/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;
    private final JwtService jwtService;

    @PostMapping
    public ResponseEntity<ApiResponse<CourseResponse>> create(@Valid @RequestBody CourseRequest request,
                                                              @RequestHeader("Authorization") String authorization) {
        Claims claims = jwtService.extractAllClaims(authorization.replace("Bearer ", ""));
        String role = claims.get("role", String.class);
        if (!"TEACHER".equals(role) && !"ADMIN".equals(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiResponse<>(403, true, "Only teachers and admins can create courses", null));
        }
        CourseResponse course = courseService.createCourse(request, claims.getSubject());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(201, false, "Course created", course));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CourseResponse>> update(@PathVariable String id,
                                                              @Valid @RequestBody CourseRequest request,
                                                              @RequestHeader("Authorization") String authorization) {
        Claims claims = jwtService.extractAllClaims(authorization.replace("Bearer ", ""));
        String role = claims.get("role", String.class);
        CourseResponse course = courseService.updateCourse(id, request, claims.getSubject(), role);
        return ResponseEntity.ok(new ApiResponse<>(200, false, "Course updated", course));
    }

    @PostMapping("/{id}/publish")
    public ResponseEntity<ApiResponse<CourseResponse>> publish(@PathVariable String id,
                                                               @RequestHeader("Authorization") String authorization) {
        Claims claims = jwtService.extractAllClaims(authorization.replace("Bearer ", ""));
        String role = claims.get("role", String.class);
        if (!"TEACHER".equals(role) && !"ADMIN".equals(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiResponse<>(403, true, "Only teachers and admins can publish", null));
        }
        CourseResponse course = courseService.publishCourse(id, claims.getSubject(), role);
        return ResponseEntity.ok(new ApiResponse<>(200, false, "Course published", course));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<CourseResponse>>> list() {
        List<CourseResponse> courses = courseService.listPublished();
        return ResponseEntity.ok(new ApiResponse<>(200, false, "Courses", courses));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CourseResponse>> get(@PathVariable String id) {
        CourseResponse course = courseService.findById(id);
        return ResponseEntity.ok(new ApiResponse<>(200, false, "Course", course));
    }
}
