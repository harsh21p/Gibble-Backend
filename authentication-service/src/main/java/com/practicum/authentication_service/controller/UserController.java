package com.practicum.authentication_service.controller;

import com.practicum.authentication_service.Model.*;
import com.practicum.authentication_service.repository.EnrollmentRepository;
import com.practicum.authentication_service.service.AESKeyStore;
import com.practicum.authentication_service.service.AESUtil;
import com.practicum.authentication_service.service.JwtService;
import com.practicum.authentication_service.service.UserService;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/api/authentication")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @Autowired
    private final EnrollmentRepository enrollmentRepository;

    @Autowired
    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    @Autowired
    private AESKeyStore aesKeyStore;


    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<User>> createUser(@RequestBody SignupRequest userCreate) {
        try {
            ApiResponse<User> response = new ApiResponse<>(200,false, "Data fetched successfully",
                    userService.createUser(userCreate)
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception exception){
            ApiResponse<User> response = new ApiResponse<>(409,true, "Failed to fetch data: " + exception.getMessage(), null);
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }
    }




    @PostMapping("/login")
    public ResponseEntity<ApiResponse<Token>> login(@RequestBody SignupRequest userCreate) {
        try {

            Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        userCreate.getEmail(),
                        userCreate.getPassword())
            );
            Optional<User> user = enrollmentRepository.findByEmail(userCreate.getEmail());

                if (authenticate.isAuthenticated() && user.isPresent()) {
                    String role = user.get().getRole().getName();
                    Map<String,String> map = new HashMap<>();
                    map.putAll(user.get().getAttributes());
                    map.put("role", role);
                    ApiResponse<Token> response = new ApiResponse<>(200, false, "Data fetched successfully", Token.builder().token(
                            jwtService.createToken(map,
                                    userCreate.getEmail())).build()
                    );
                return ResponseEntity.status(HttpStatus.OK).body(response);
                } else {
                ApiResponse<Token> response = new ApiResponse<>(401, true, "Failed to fetch data: invalid credentials", null);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
             }
        } catch (Exception exception){
            ApiResponse<Token> response = new ApiResponse<>(401, true, "Failed to fetch data: invalid credentials", null);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }


    @PostMapping("/login-aes")
    public ResponseEntity<ApiResponse<Token>> loginAes(@RequestBody SignupRequest userCreate) {
        try {
            String email = AESUtil.decrypt(userCreate.getEmail(),aesKeyStore.get(userCreate.getSessionId()).getKey(),aesKeyStore.get(userCreate.getSessionId()).getIv());
            String pass = AESUtil.decrypt(userCreate.getPassword(),aesKeyStore.get(userCreate.getSessionId()).getKey(),aesKeyStore.get(userCreate.getSessionId()).getIv());
            Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email,pass));
            Optional<User> user = enrollmentRepository.findByEmail(email);

            if (authenticate.isAuthenticated() && user.isPresent()) {
                String role = user.get().getRole().getName();
                Map<String,String> map = new HashMap<>();
                map.putAll(user.get().getAttributes());
                map.put("role", role);
                ApiResponse<Token> response = new ApiResponse<>(200, false, "Data fetched successfully", Token.builder().token(
                        jwtService.createToken(map,email)).build()
                );
                return ResponseEntity.status(HttpStatus.OK).body(response);
            } else {
                ApiResponse<Token> response = new ApiResponse<>(401, true, "Failed to fetch data: invalid credentials", null);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
        } catch (Exception exception){
            ApiResponse<Token> response = new ApiResponse<>(401, true, "Failed to fetch data: invalid credentials", null);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }


    @PostMapping("/update")
    public ResponseEntity<ApiResponse<User>> update(@RequestBody RoleUpdate roleUpdate, @RequestHeader Map<String, String> headers, @RequestHeader("Authorization") String authHeader){
        String token = authHeader.startsWith("Bearer ") ? authHeader.substring(7) : authHeader;
        Map<String,String> map = jwtService.extractAttributes(token);
        if(Objects.equals(map.get("role"), "ADMIN")){
            try {
                ApiResponse<User> response = new ApiResponse<>(200,false, "Data fetched successfully",
                        userService.updateUser(roleUpdate)
                );
                return ResponseEntity.status(HttpStatus.CREATED).body(response);
            } catch (Exception exception){
                ApiResponse<User> response = new ApiResponse<>(204,true, "Failed to fetch data: " + exception.getMessage(), null);
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
            }
        } else {
            ApiResponse<User> response = new ApiResponse<>(401,true,  "UNAUTHORIZED", null);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }
}
