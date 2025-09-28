package com.practicum.inventory_service.repository;

import com.practicum.inventory_service.entities.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, String> {
    boolean existsByName(String dep);
    Optional<Department> findById(String id);
}
