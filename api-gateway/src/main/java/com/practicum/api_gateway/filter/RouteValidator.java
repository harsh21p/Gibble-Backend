package com.practicum.api_gateway.filter;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;

@Component
public class RouteValidator {

    public static final List<String> openApiEndpoints = List.of(
            "/api/authentication/auth/validate",
            "/api/authentication/signup",
            "/api/authentication/login",
            "/api/users/adduserdetails",
            "/api/authentication/keys/exchange-key",
            "/api/authentication/keys/exchange-key-rsa-test",
            "/api/authentication/keys/exchange-key-aes-enc-test",
            "/api/authentication/login-aes"

    );

    public Predicate<ServerHttpRequest> isSecured =
            request -> openApiEndpoints
                    .stream()
                    .noneMatch(uri -> request.getURI().getPath().contains(uri));

}