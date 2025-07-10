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

    @GetMapping("/get-all")
    public List<User> findAll() {
        List<User> response = userService.getAll();
        if (response.isEmpty()) {
            ResponseEntity.notFound().build();
            return null;
        } else {
            return response;
        }
    }

    @PostMapping("/save")
    public ResponseEntity<User> save(@RequestBody User user) {
        User savedUser = userService.save(user);
        return ResponseEntity.ok(savedUser);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<User> update(@PathVariable Long id, @RequestBody User user) {
        user.setId(id); // Gelen user objesine path’den gelen id’yi set et
        User response = userService.update(id,user);
        return ResponseEntity.ok(response);
    }


}
