package com.practicum.authentication_service.service;

import com.practicum.authentication_service.Model.*;
import com.practicum.authentication_service.repository.EnrollmentRepository;
import com.practicum.authentication_service.repository.RoleRepository;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final EnrollmentRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    @Autowired
    private WebClient.Builder webClientBuilder;

    public User createUser(SignupRequest userCreate) throws Exception {
        try {
            Optional<Role> defaultRole = roleRepository.findByName("USER");

            if(defaultRole.isPresent()) {
                User user =  User.builder()
                    .email(userCreate.getEmail())
                    .password(passwordEncoder.encode(userCreate.getPassword()))
                    .role(defaultRole.get())
                        .build();
                User u = userRepository.save(user);

                ParameterizedTypeReference<String> responseType = new ParameterizedTypeReference<>() {};
                Mono<ResponseEntity<String>> responseEntityMono = webClientBuilder.build()
                        .post()
                        .uri("http://user-service/api/users/adduserdetails")
                        .contentType(MediaType.valueOf(MediaType.APPLICATION_JSON_VALUE))
                        .bodyValue(
                                UserDetails.builder()
                                        .userId(u.getId())
                                        .username(userCreate.getUsername())
                                        .email(userCreate.getEmail())
                                        .address(userCreate.getAddress())
                                        .lastName(userCreate.getLastName())
                                        .firstName(userCreate.getFirstName())
                                        .build())
                        .retrieve()
                        .toEntity(responseType);

                ResponseEntity<String> responseEntity = responseEntityMono.block();
                if(responseEntity.getStatusCode().is2xxSuccessful()) {
                    if(responseEntity.getStatusCode().is2xxSuccessful()){
                        return user;
                    } else {
                        throw  new Exception("failed to save user");
                    }
                } else {
                    throw  new Exception("Error from user service save user");
                }

            }
            throw  new Exception("Error in saving user");
        } catch (Exception exception) {
            throw exception;
        }
    }

    public User updateUser(RoleUpdate roleUpdate) throws Exception {
        try {
            Optional<User> user = userRepository.findByEmail(roleUpdate.getEmail());
            Optional<Role> defaultRole = roleRepository.findByName(roleUpdate.getRole());
            if(user.isPresent() && defaultRole.isPresent()) {
                user.get().setRole(defaultRole.get());
                Map<String ,String > map = new HashMap<>();
                map.put("department",roleUpdate.getDepartment());
                map.put("region",roleUpdate.getRegion());
                user.get().setAttributes(map);
                return userRepository.save(user.get());
            } else  {
                throw  new Exception("not found");
            }
        } catch (Exception e){
            throw e;
        }
    }
}
