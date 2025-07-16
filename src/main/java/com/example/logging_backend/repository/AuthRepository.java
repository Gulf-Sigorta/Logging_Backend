package com.example.logging_backend.repository;

import com.example.logging_backend.model.Auth.Auth;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthRepository extends JpaRepository<Auth,Long> {

    Auth findByUsername(String username);
}
