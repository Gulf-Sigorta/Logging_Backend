package com.example.logging_backend.service;

import com.example.logging_backend.model.User;
import com.example.logging_backend.repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User login(String username, String password) {
        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("Hata");
        }
        if (!user.getPassword().equals(password)) {
            throw new RuntimeException("Şifre yanlış");
        }

        return user;
    }
}
