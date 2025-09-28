package com.practicum.inventory_service.controller;

import com.practicum.inventory_service.config.JwtService;
import com.practicum.inventory_service.entities.ApiResponse;
import com.practicum.inventory_service.entities.Department;
import com.practicum.inventory_service.entities.Region;
import com.practicum.inventory_service.repository.DepartmentRepository;
import com.practicum.inventory_service.repository.RegionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/inventory/helper")
public class HelperController {
    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private RegionRepository regionRepository;

    @Autowired
    private JwtService jwtService;

    private boolean isAdmin(Map<String, String> claims) {
        return "ADMIN".equals(claims.get("role"));
    }

    @PostMapping("/department")
    public ResponseEntity<ApiResponse<Department>> createDepartment(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody Department department) {

        Map<String, String> claims = jwtService.extractAttributes(authHeader.substring(7));

        if (!isAdmin(claims)) {
            return ResponseEntity.status(403)
                    .body(new ApiResponse<>(403, true, "Access denied: ADMIN only", null));
        }
        try {
            Department saved = departmentRepository.save(department);
            return ResponseEntity.ok(new ApiResponse<>(200, false, "Department created", saved));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse<>(409, true, e.getMessage(), null));
        }
    }

    @GetMapping("/departments")
    public ResponseEntity<ApiResponse<List<Department>>> getDepartments(
            @RequestHeader("Authorization") String authHeader) {

        Map<String, String> claims = jwtService.extractAttributes(authHeader.substring(7));
        if (!isAdmin(claims)) {
            return ResponseEntity.status(403)
                    .body(new ApiResponse<>(403, true, "Access denied: ADMIN only", null));
        }
        try {
            List<Department> list = departmentRepository.findAll();
            return ResponseEntity.ok(new ApiResponse<>(200, false, "Departments fetched", list));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse<>(409, true, e.getMessage(), null));
        }

    }

    @PostMapping("/region")
    public ResponseEntity<ApiResponse<Region>> createRegion(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody Region region) {

        Map<String, String> claims = jwtService.extractAttributes(authHeader.substring(7));
        if (!isAdmin(claims)) {
            return ResponseEntity.status(403)
                    .body(new ApiResponse<>(403, true, "Access denied: ADMIN only", null));
        }
        try {
            Region saved = regionRepository.save(region);
            return ResponseEntity.ok(new ApiResponse<>(200, false, "Region created", saved));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse<>(409, true, e.getMessage(), null));
        }
    }

    @GetMapping("/region")
    public ResponseEntity<ApiResponse<List<Region>>> getRegions(
            @RequestHeader("Authorization") String authHeader) {

        Map<String, String> claims = jwtService.extractAttributes(authHeader.substring(7));
        if (!isAdmin(claims)) {
            return ResponseEntity.status(403)
                    .body(new ApiResponse<>(403, true, "Access denied: ADMIN only", null));
        }

        try {
            List<Region> list = regionRepository.findAll();
            return ResponseEntity.ok(new ApiResponse<>(200, false, "Regions fetched", list));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse<>(409, true, e.getMessage(), null));
        }
    }
}
