package com.example.eduLearn.controller;

import com.example.eduLearn.dto.request.AuthRequest;
import com.example.eduLearn.dto.request.SignupRequest;
import com.example.eduLearn.dto.response.AuthResponse;
import com.example.eduLearn.service.AuthService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }
    
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @Valid @RequestBody AuthRequest request) {
        
        AuthResponse response = authService.signInWithEmail(request);
        if (response.getError() != null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> signup(
            @Validated @RequestBody SignupRequest request) {
        
        // Validate password match
        if (!request.getPassword().equals(request.getRepassword())) {
            AuthResponse errorResponse = new AuthResponse();
            errorResponse.setError("Passwords do not match");
            return ResponseEntity.badRequest().body(errorResponse);
        }

        return ResponseEntity.ok(authService.signUp(request));
    }
}