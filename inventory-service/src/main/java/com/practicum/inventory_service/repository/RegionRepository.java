package com.practicum.inventory_service.repository;

import com.practicum.inventory_service.entities.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RegionRepository  extends JpaRepository<Region, String> {
    boolean existsByName(String regionName);
    Optional<Region> findById(String id);
}
