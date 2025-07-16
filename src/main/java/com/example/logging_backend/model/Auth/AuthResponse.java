package com.example.logging_backend.model.Auth;


public class AuthResponse {
    private boolean isAuthenticated;

    public AuthResponse(boolean isAuthenticated) {
        this.isAuthenticated = isAuthenticated;
    }

    public boolean getIsAuthenticated() {
        return isAuthenticated;
    }

    public void setIsAuthenticated(boolean isAuthenticated) {
        this.isAuthenticated = isAuthenticated;
    }
}
