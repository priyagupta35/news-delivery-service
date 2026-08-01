package com.techpulse.delivery.dto;

public class AuthResponse {

    private String token;
    private String role;
    private String message;
    private String type = "Bearer";

    // Three argument constructor matching AuthService calls
    public AuthResponse(String token, String role, String message) {
        this.token = token;
        this.role = role;
        this.message = message;
    }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
}