package com.education.api.controller;


import com.education.api.dto.request.LoginRequest;
import com.education.api.dto.response.LoginResponse;
import com.education.api.entity.User;
import com.education.api.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository userRepository;

    public AuthController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/register")  // <-- THIS WAS MISSING
    public ResponseEntity<?> register(@RequestBody User user) {
        userRepository.save(user);
        return ResponseEntity.status(201).body(Map.of("message", "User registered successfully"));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        return userRepository.findByEmail(request.getEmail())
            .filter(u -> u.getPassword().equals(request.getPassword()))
            .map(u -> ResponseEntity.ok(new LoginResponse("Login successful", u.getId(), u.getEmail())))
            .orElse(ResponseEntity.status(401).body(new LoginResponse("Invalid email or password", null, null)));
    }
}