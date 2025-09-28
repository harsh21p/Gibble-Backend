package com.practicum.inventory_service;

import com.practicum.inventory_service.entities.Department;
import com.practicum.inventory_service.entities.Region;
import com.practicum.inventory_service.repository.DepartmentRepository;
import com.practicum.inventory_service.repository.RegionRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@SpringBootApplication
@EnableDiscoveryClient
public class InventoryServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(InventoryServiceApplication.class, args);
	}

	@Bean
	public CommandLineRunner loadData(RegionRepository regionRepository, DepartmentRepository departmentRepository) {
		return args -> {
			String[] roles = {"IND", "USA", "EU"};
			String[] department = {"ENG", "TECH", "HR"};

			for (String reg : roles) {
				if (!regionRepository.existsByName(reg)) {
					Region region = Region.builder()
							.name(reg)
							.build();
					regionRepository.save(region);
				}
			}

			for (String dep : department) {
				if (!departmentRepository.existsByName(dep)) {
					Department department1 = Department.builder()
							.name(dep)
							.build();
					departmentRepository.save(department1);
				}
			}
		};
	}
}
