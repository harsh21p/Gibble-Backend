package com.practicum.authentication_service;

import com.practicum.authentication_service.Model.User;
import com.practicum.authentication_service.repository.EnrollmentRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

import com.practicum.authentication_service.Model.Role;
import com.practicum.authentication_service.repository.RoleRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@SpringBootApplication
@EnableDiscoveryClient
public class AuthenticationServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthenticationServiceApplication.class, args);
	}

	@Bean
	public CommandLineRunner loadData(RoleRepository roleRepository, EnrollmentRepository enrollmentRepository, PasswordEncoder passwordEncoder) {
		return args -> {
			String[] roles = {"ADMIN", "USER", "MANAGER"};

			for (String roleName : roles) {
				if (!roleRepository.existsByName(roleName)) {
					Role role = Role.builder()
							.name(roleName)
							.build();
					roleRepository.save(role);
				}
			}


			if (!enrollmentRepository.existsByEmail("admin@admin.com")) {
				Optional<Role> role = roleRepository.findByName("ADMIN");
				if (role.isPresent()) {
					User user = User.builder().email("admin@admin.com").role(
						role.get()
					).password(passwordEncoder.encode("admin@123")).build();
					enrollmentRepository.save(user);
				}
			}

		};
	}

}
