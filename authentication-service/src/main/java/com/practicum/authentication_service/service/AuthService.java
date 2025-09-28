package com.practicum.authentication_service.service;

import com.practicum.authentication_service.Model.AuthResponse;
import com.practicum.authentication_service.Model.Token;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final JwtService jwtService;

    public String validateToken(Token token) throws Exception {
        try {
            jwtService.validateToken(token.getToken());
            return "Valid Token!";
        } catch (Exception exception){
            throw exception;
        }
    }

}
