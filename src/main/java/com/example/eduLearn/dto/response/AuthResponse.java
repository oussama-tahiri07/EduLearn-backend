package com.example.eduLearn.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty; // Add this import

public class AuthResponse {
    @JsonProperty("access_token") // Maps to Supabase's JSON field
    private String accessToken;
    
    @JsonProperty("refresh_token") // Maps to Supabase's JSON field
    private String refreshToken;
    
    private User user; // Nested user object
    private String error;

    // Nested User class
    public static class User {
        private String id;
        private String email;
        
        // Getters and setters
        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
    }

    // Getters and setters (updated)
    public String getAccessToken() { return accessToken; }
    public void setAccessToken(String accessToken) { this.accessToken = accessToken; }
    
    public String getRefreshToken() { return refreshToken; }
    public void setRefreshToken(String refreshToken) { this.refreshToken = refreshToken; }
    
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    
    public String getError() { return error; }
    public void setError(String error) { this.error = error; }

    // Helper method to get userId
    public String getUserId() {
        return user != null ? user.getId() : null;
    }
}