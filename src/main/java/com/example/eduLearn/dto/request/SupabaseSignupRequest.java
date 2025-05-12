package com.example.eduLearn.dto.request;

import lombok.Data;

@Data
public class SupabaseSignupRequest {
    private String email;
    private String password;
    private UserData user_metadata;
    private String role;

    public SupabaseSignupRequest(String email, String password, String username, String role) {
        this.email = email;
        this.password = password;
        this.user_metadata = new UserData(username);
        this.role = role;
    }

    @Data
    private static class UserData {
        private String username;

        public UserData(String username) {
            this.username = username;
        }
    }
}