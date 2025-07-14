package com.example.logging_backend.controller;

import com.example.logging_backend.model.User;
import com.example.logging_backend.model.UserRequest;
import com.example.logging_backend.model.UserResponse;
import com.example.logging_backend.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserRequest request) {
        try {
            String token = userService.loginAndGenerateToken(request.getUsername(), request.getPassword());
            return ResponseEntity.ok(new UserResponse(token));
        } catch (Exception e) {
            return ResponseEntity.status(401).body(e.getMessage());
        }
    }
}
