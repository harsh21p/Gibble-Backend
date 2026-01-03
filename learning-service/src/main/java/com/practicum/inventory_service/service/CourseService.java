package com.practicum.inventory_service.service;

import com.practicum.inventory_service.domain.Course;
import com.practicum.inventory_service.domain.CourseStatus;
import com.practicum.inventory_service.dto.CourseRequest;
import com.practicum.inventory_service.dto.CourseResponse;
import com.practicum.inventory_service.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;

    @Transactional
    @CacheEvict(cacheNames = {"courseCatalog", "course"}, allEntries = true)
    public CourseResponse createCourse(CourseRequest request, String teacherId) {
        Course course = courseRepository.save(Course.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .teacherId(teacherId)
                .category(request.getCategory())
                .level(request.getLevel())
                .status(CourseStatus.DRAFT)
                .build());
        return toResponse(course);
    }

    @Transactional
    @CacheEvict(cacheNames = {"courseCatalog", "course"}, allEntries = true)
    public CourseResponse publishCourse(String courseId, String requesterId, String role) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Course not found"));
        if (!requesterId.equals(course.getTeacherId()) && !"ADMIN".equals(role)) {
            throw new IllegalArgumentException("Only the course owner or admin can publish");
        }
        course.setStatus(CourseStatus.PUBLISHED);
        course.setPublishedAt(java.time.Instant.now());
        return toResponse(courseRepository.save(course));
    }

    @Transactional
    @CacheEvict(cacheNames = {"courseCatalog", "course"}, allEntries = true)
    public CourseResponse updateCourse(String id, CourseRequest request, String requesterId, String role) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Course not found"));
        if (!requesterId.equals(course.getTeacherId()) && !"ADMIN".equals(role)) {
            throw new IllegalArgumentException("Only the course owner or admin can update");
        }
        course.setTitle(request.getTitle());
        course.setDescription(request.getDescription());
        course.setCategory(request.getCategory());
        course.setLevel(request.getLevel());
        return toResponse(courseRepository.save(course));
    }

    @Cacheable(cacheNames = "courseCatalog")
    public List<CourseResponse> listPublished() {
        return courseRepository.findByStatus(CourseStatus.PUBLISHED)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Cacheable(cacheNames = "course", key = "#id")
    public CourseResponse findById(String id) {
        return courseRepository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new IllegalArgumentException("Course not found"));
    }

    private CourseResponse toResponse(Course course) {
        return CourseResponse.builder()
                .id(course.getId())
                .code(course.getCode())
                .title(course.getTitle())
                .description(course.getDescription())
                .teacherId(course.getTeacherId())
                .category(course.getCategory())
                .level(course.getLevel())
                .status(course.getStatus())
                .build();
    }
}
