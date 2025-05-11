package com.example.eduLearn.service.impl;

import com.example.eduLearn.dto.*;
import com.example.eduLearn.dto.request.AuthRequest;
import com.example.eduLearn.dto.request.SignupRequest;
import com.example.eduLearn.dto.request.SupabaseSignupRequest;
import com.example.eduLearn.dto.response.AuthResponse;
import com.example.eduLearn.service.AuthService;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class SupabaseAuthServiceImpl implements AuthService {

    @Value("${supabase.url}")
    private String supabaseUrl;

    @Value("${supabase.key}")
    private String supabaseKey;

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public AuthResponse signUp(SignupRequest request) {
        String url = supabaseUrl + "/auth/v1/signup";

        // Create Supabase-compatible request
        SupabaseSignupRequest supabaseRequest = new SupabaseSignupRequest(
                request.getEmail(),
                request.getPassword(),
                request.getUsername()
        );

        HttpHeaders headers = new HttpHeaders();
        headers.set("apikey", supabaseKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<SupabaseSignupRequest> entity = new HttpEntity<>(supabaseRequest, headers);

        try {
            ResponseEntity<AuthResponse> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    entity,
                    AuthResponse.class
            );

            if (response.getStatusCode().is2xxSuccessful()) {
                AuthResponse authResponse = response.getBody();
                // Additional processing if needed
                return authResponse;
            } else {
                return handleErrorResponse(response);
            }
        } catch (Exception e) {
            return handleException(e);
        }
    }
    
    @Override
    public AuthResponse signInWithEmail(AuthRequest request) {
        String url = supabaseUrl + "/auth/v1/token?grant_type=password";
        
        // Create Supabase-compatible request
        Map<String, String> supabaseRequest = new HashMap<>();
        supabaseRequest.put("email", request.getEmail());
        supabaseRequest.put("password", request.getPassword());
        
        HttpHeaders headers = new HttpHeaders();
        headers.set("apikey", supabaseKey);
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        HttpEntity<Map<String, String>> entity = new HttpEntity<>(supabaseRequest, headers);
        
        try {
            ResponseEntity<AuthResponse> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    entity,
                    AuthResponse.class
            );
            
            if (response.getStatusCode().is2xxSuccessful()) {
                AuthResponse authResponse = response.getBody();
                // Additional processing if needed
                return authResponse;
            } else {
                return handleErrorResponse(response);
            }
        } catch (Exception e) {
            return handleException(e);
        }
    }

    private AuthResponse handleErrorResponse(ResponseEntity<AuthResponse> response) {
        AuthResponse errorResponse = new AuthResponse();
        errorResponse.setError("Registration failed: " + 
            (response.getBody() != null ? 
             response.getBody().getError() : 
             "Unknown error"));
        return errorResponse;
    }

    private AuthResponse handleException(Exception e) {
        AuthResponse errorResponse = new AuthResponse();
        errorResponse.setError("Registration error: " + e.getMessage());
        return errorResponse;
    }

}