package com.example.eduLearn.service;

import com.example.eduLearn.dto.request.AuthRequest;
import com.example.eduLearn.dto.request.SignupRequest;
import com.example.eduLearn.dto.response.AuthResponse;

public interface AuthService {
    AuthResponse signUp(SignupRequest request);
    AuthResponse signInWithEmail(AuthRequest request);
    AuthResponse updateUserRole(String userId, String newRole);
}