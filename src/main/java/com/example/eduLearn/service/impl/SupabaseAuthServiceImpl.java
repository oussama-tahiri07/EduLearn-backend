package com.example.eduLearn.service.impl;

import com.example.eduLearn.dto.request.AuthRequest;
import com.example.eduLearn.dto.request.SignupRequest;
import com.example.eduLearn.dto.response.AuthResponse;
import com.example.eduLearn.service.AuthService;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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

        // Force RESTRICTED role for new signups (override any client-provided role)
        String assignedRole = "RESTRICTED";
        
        // Create user metadata with role
        Map<String, Object> userMetadata = new HashMap<>();
        userMetadata.put("username", request.getUsername());
        userMetadata.put("role", assignedRole);

        // Create Supabase-compatible request
        Map<String, Object> supabaseRequest = new HashMap<>();
        supabaseRequest.put("email", request.getEmail());
        supabaseRequest.put("password", request.getPassword());
        supabaseRequest.put("data", userMetadata);  // Metadata including role

        HttpHeaders headers = new HttpHeaders();
        headers.set("apikey", supabaseKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(supabaseRequest, headers);

        try {
            ResponseEntity<AuthResponse> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    entity,
                    AuthResponse.class
            );

            if (response.getStatusCode().is2xxSuccessful()) {
                AuthResponse authResponse = response.getBody();
                
                // Optionally store additional user info in your database
                // userRepository.save(new User(authResponse.getUserId(), request.getEmail(), assignedRole));
                
                return authResponse;
            } else {
                return handleErrorResponse(response, "Sign up");
            }
        } catch (Exception e) {
            return handleException(e, "Sign up");
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
                return handleErrorResponse(response, "Sign In");
            }
        } catch (Exception e) {
            return handleException(e, "Sign In");
        }
    }
    
    @Override
    public AuthResponse updateUserRole(String userId, String newRole) {
        // Validate inputs
        if (userId == null || userId.isEmpty()) {
            AuthResponse error = new AuthResponse();
            error.setError("User ID cannot be empty");
            return error;
        }

        String url = supabaseUrl + "/auth/v1/admin/users/" + userId;
        
        // Prepare the role update request
        Map<String, Object> updateRequest = new HashMap<>();
        Map<String, Object> userMetadata = new HashMap<>();
        userMetadata.put("role", newRole);
        updateRequest.put("user_metadata", userMetadata);
        
        HttpHeaders headers = new HttpHeaders();
        headers.set("apikey", supabaseKey);
        headers.set("Authorization", "Bearer " + getServiceRoleToken());
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        try {
            ResponseEntity<AuthResponse> response = restTemplate.exchange(
                url,
                HttpMethod.PUT,
                new HttpEntity<>(updateRequest, headers),
                AuthResponse.class
            );
            
            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            } else {
                return handleErrorResponse(response, "Role update");
            }
        } catch (Exception e) {
            return handleException(e, "Role update");
        }
    }

    private String getServiceRoleToken() {
        // Use Supabase's service role key (from your config)
        // NEVER expose this to frontend - only for backend admin operations
        return supabaseKey; // This should actually be your service_role key, not anon/public key
    }

    private boolean isValidRole(String role) {
        // Define your valid roles
        Set<String> validRoles = Set.of("RESTRICTED", "USER", "EDITOR", "ADMIN");
        return validRoles.contains(role);
    }

    private AuthResponse createErrorResponse(String message) {
        AuthResponse response = new AuthResponse();
        response.setError(message);
        return response;
    }

    private AuthResponse handleErrorResponse(ResponseEntity<AuthResponse> response, String operation) {
        AuthResponse errorResponse = new AuthResponse();
        errorResponse.setError(operation + " failed: " + 
            (response.getBody() != null ? 
             response.getBody().getError() : 
             "Status " + response.getStatusCode()));
        return errorResponse;
    }

    private AuthResponse handleException(Exception e, String operation) {
        AuthResponse errorResponse = new AuthResponse();
        errorResponse.setError(operation + " error: " + e.getMessage());
        return errorResponse;
    }

}