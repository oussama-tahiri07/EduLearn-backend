package com.example.eduLearn.dto.request;

import lombok.Data;
import jakarta.validation.constraints.*;

@Data
public class SignupRequest {
    @NotBlank(message = "Username is required")
    private String username;

    @Email(message = "Email should be valid")
    @NotBlank(message = "Email is required")
    private String email;

    @Size(min = 6, message = "Password should be at least 6 characters")
    @NotBlank(message = "Password is required")
    private String password;

    @NotBlank(message = "Password confirmation is required")
    private String repassword;
    
    private String role = "RESTRICTED";
    
    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getRepassword() {
        return repassword;
    }

    // Setters
    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRepassword(String repassword) {
        this.repassword = repassword;
    }
}