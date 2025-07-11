package com.example.logging_backend.controller;

import com.example.logging_backend.model.User;
import com.example.logging_backend.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody User user) {
        User response = userService.login(user.getUsername(), user.getPassword());
        return ResponseEntity.ok(response);
    }
}
