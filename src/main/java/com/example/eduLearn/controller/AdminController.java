package com.example.eduLearn.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.eduLearn.dto.response.AuthResponse;
import com.example.eduLearn.service.impl.SupabaseAuthServiceImpl;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    
    private final SupabaseAuthServiceImpl authService;

    public AdminController(SupabaseAuthServiceImpl authService) {
        this.authService = authService;
    }

    @PostMapping("/update-role")
    public ResponseEntity<AuthResponse> updateUserRole(
            @RequestParam String userId,
            @RequestParam String newRole) {
        
        // In a real app, add admin authorization check here
        
        AuthResponse response = authService.updateUserRole(userId, newRole);
        if (response.getError() != null) {
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok(response);
    }
}
