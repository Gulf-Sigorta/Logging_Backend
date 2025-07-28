package com.example.logging_backend.repository;

import com.example.logging_backend.model.UserSession;
import com.example.logging_backend.model.Auth;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserSessionRepository extends JpaRepository<UserSession, Long> {
    Optional<UserSession> findTopByUserOrderByLoginTimeDesc(Auth user);
    Optional<UserSession> findTopByUserAndIsActiveTrueOrderByLoginTimeDesc(Auth user);
}
