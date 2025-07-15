package com.example.logging_backend.repository;

import com.example.logging_backend.model.Auth.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthRepository extends JpaRepository<User,Long> {

    User findByUsername(String username);
}
