package com.example.eduLearn.dto.request;

import lombok.Data;

@Data
public class SupabaseSignupRequest {
    private String email;
    private String password;
    private UserData user_metadata;

    public SupabaseSignupRequest(String email, String password, String username) {
        this.email = email;
        this.password = password;
        this.user_metadata = new UserData(username);
    }

    @Data
    private static class UserData {
        private String username;

        public UserData(String username) {
            this.username = username;
        }
    }
}