package com.practicum.user_service.controller;

import com.practicum.user_service.config.JwtService;
import com.practicum.user_service.entities.User;

import com.practicum.user_service.models.ApiResponse;
import com.practicum.user_service.models.UserDetails;
import com.practicum.user_service.repository.UserRepository;
import com.practicum.user_service.service.userService;
import com.practicum.user_service.service.userServiceImpl;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserRepository userRepository;
    @Autowired
    private userServiceImpl userServiceImpl;

    @Autowired
    private final JwtService jwtService;

    @GetMapping("/findUserById/{id}")
    public ResponseEntity<ApiResponse<User>> findUserById(@PathVariable String id, @RequestHeader Map<String, String> headers, @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.startsWith("Bearer ") ? authHeader.substring(7) : authHeader;
        Map<String, String> map = jwtService.extractAttributes(token);
        if (Objects.equals(map.get("role"), "MANAGER") || Objects.equals(map.get("role"), "ADMIN")) {
            Optional<User> user = userRepository.findUserByUserId(id);
            if (user.isPresent()) {
                ApiResponse<User> response = new ApiResponse<User>(200, false, "found", user.get());
                return ResponseEntity.ok(response);
            } else {
                ApiResponse<User> response = new ApiResponse<User>(203, true, "not found", null);
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<User> updateUser(@PathVariable String id, @RequestBody User user) {
        User existingUser = userRepository.findUserById(id);
        if (existingUser != null) {
            existingUser.setAddress(user.getAddress());
            existingUser.setFirstName(user.getFirstName());
            existingUser.setLastName(user.getLastName());
            userRepository.save(existingUser);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(existingUser);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<User> deleteUser(@PathVariable String id) {
        User existingUser = userRepository.findUserById(id);
        if (existingUser != null) {
            userRepository.deleteById(id);
        } else {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(existingUser);
    }


    @PostMapping("/adduserdetails")
    public ResponseEntity<String> addUserDetails(@RequestBody UserDetails userDetails) {
        try {
            User user = new User();
            user.setFirstName(userDetails.getFirstName());
            user.setLastName(userDetails.getLastName());
            user.setAddress(userDetails.getAddress());
            user.setUserId(userDetails.getUserId());
            user.setEmail(userDetails.getEmail());
            user.setUsername(userDetails.getUsername());
            try {
                User u = userRepository.save(user);
                return ResponseEntity.ok(u.getId());
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
