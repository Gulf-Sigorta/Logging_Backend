package com.example.logging_backend.model;

import com.example.logging_backend.model.Auth.Auth;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Duration;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "user_sessions")
public class UserSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Auth user;

    private LocalDateTime loginTime;

    private LocalDateTime logoutTime;

    private Duration sessionDuration;

    private Boolean isActive = true;
}
