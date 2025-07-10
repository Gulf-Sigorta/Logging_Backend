package com.example.logging_backend.service;

import com.example.logging_backend.model.User;
import com.example.logging_backend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public User save(User user) {
        userRepository.save(user);
        return user;
    }

    public User update(Long id, User user) {
        Optional<User> oldUser = userRepository.findById(id);
        oldUser.ifPresent(value -> value.setUsername(user.getUsername()));

        userRepository.save(oldUser.get());
        return user;
    }
}
