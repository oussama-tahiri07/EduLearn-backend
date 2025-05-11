package com.example.eduLearn.service;

import com.example.eduLearn.dto.request.AuthRequest;
import com.example.eduLearn.dto.request.SignupRequest;
import com.example.eduLearn.dto.response.AuthResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class SupabaseAuthService {

    @Value("${supabase.url}")
    private String supabaseUrl;

    @Value("${supabase.key}")
    private String supabaseKey;

    private final RestTemplate restTemplate = new RestTemplate();

    public AuthResponse signInWithEmail(AuthRequest request) {
        String url = supabaseUrl + "/auth/v1/token?grant_type=password";
        HttpEntity<AuthRequest> entity = new HttpEntity<>(request, createAuthHeaders());
        
        try {
            ResponseEntity<AuthResponse> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                entity,
                AuthResponse.class
            );
            return processAuthResponse(response);
        } catch (HttpClientErrorException e) {
            return createErrorResponse("Login failed: " + e.getResponseBodyAsString());
        }
    }

    public AuthResponse signUpWithEmail(SignupRequest request) {
        // 1. Validate password match first
        if (!request.getPassword().equals(request.getRepassword())) {
            return createErrorResponse("Passwords do not match");
        }

        // 2. Create auth user in Supabase
        AuthResponse authResponse = createAuthUser(request);
        if (authResponse.getError() != null) {
            return authResponse;
        }

        // 3. Create user profile
        try {
            createUserProfile(authResponse.getUser().getId(), request.getUsername());
            return authResponse;
        } catch (Exception e) {
            return createErrorResponse("Profile creation failed: " + e.getMessage());
        }
    }

    private AuthResponse createAuthUser(SignupRequest request) {
        String url = supabaseUrl + "/auth/v1/signup";
        Map<String, String> authBody = Map.of(
            "email", request.getEmail(),
            "password", request.getPassword()
        );

        try {
            ResponseEntity<AuthResponse> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                new HttpEntity<>(authBody, createAuthHeaders()),
                AuthResponse.class
            );
            return processAuthResponse(response);
        } catch (HttpClientErrorException e) {
            return createErrorResponse("Registration failed: " + e.getResponseBodyAsString());
        }
    }

    private void createUserProfile(String userId, String username) {
        String url = supabaseUrl + "/rest/v1/profiles";
        Map<String, Object> profile = Map.of(
            "id", userId,
            "username", username
        );

        restTemplate.exchange(
            url,
            HttpMethod.POST,
            new HttpEntity<>(profile, createAuthHeaders()),
            Void.class
        );
    }

    private HttpHeaders createAuthHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("apikey", supabaseKey);
        headers.set("Authorization", "Bearer " + supabaseKey);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    private AuthResponse processAuthResponse(ResponseEntity<AuthResponse> response) {
        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            return response.getBody();
        }
        return createErrorResponse("Authentication failed");
    }

    private AuthResponse createErrorResponse(String message) {
        AuthResponse response = new AuthResponse();
        response.setError(message);
        return response;
    }
}