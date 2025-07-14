package com.example.logging_backend.model;

public class UserResponse {
    private String token;

    public UserResponse(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
