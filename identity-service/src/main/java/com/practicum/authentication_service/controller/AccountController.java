package com.practicum.authentication_service.controller;

import com.practicum.authentication_service.domain.Account;
import com.practicum.authentication_service.dto.ApiResponse;
import com.practicum.authentication_service.dto.RoleUpdateRequest;
import com.practicum.authentication_service.service.IdentityService;
import com.practicum.authentication_service.service.JwtService;
import io.jsonwebtoken.Claims;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/identity/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final IdentityService identityService;
    private final JwtService jwtService;

    @PatchMapping("/{accountId}/role")
    public ResponseEntity<ApiResponse<Account>> updateRole(@PathVariable String accountId,
                                                           @Valid @RequestBody RoleUpdateRequest request,
                                                           @RequestHeader("Authorization") String authorization) {
        String token = authorization.replace("Bearer ", "");
        Claims claims = jwtService.extractAllClaims(token);
        if (!"ADMIN".equals(claims.get("role"))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiResponse<>(403, true, "Only admins can update roles", null));
        }
        request.setAccountId(accountId);
        Account account = identityService.updateRole(request);
        return ResponseEntity.ok(new ApiResponse<>(200, false, "Role updated", account));
    }
}
