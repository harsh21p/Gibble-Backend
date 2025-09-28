package com.practicum.authentication_service.controller;

import com.practicum.authentication_service.Model.ApiResponse;
import com.practicum.authentication_service.Model.Token;
import com.practicum.authentication_service.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/authentication/auth")
@RequiredArgsConstructor
public class AuthController {

    @Autowired
    private final JwtService service;

    @PostMapping("/validate")
    public ResponseEntity<ApiResponse<Boolean>> validateToken(@RequestBody Token token) {
        try {
            if(service.validateToken(token.getToken())){
                ApiResponse<Boolean> response = new ApiResponse<>(200,false, "Data fetched successfully", true);
                return ResponseEntity.status(HttpStatus.OK).body(response);
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse<>(400,false, "Invalid Token", false));
        } catch (Exception exception){
            ApiResponse<Boolean> response = new ApiResponse<>(401,true, "Failed to fetch data: " + exception.getMessage(), false);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }

}
