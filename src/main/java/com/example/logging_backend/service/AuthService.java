package com.example.logging_backend.service;

import com.example.logging_backend.model.Auth.Auth;
import com.example.logging_backend.repository.AuthRepository;
import com.example.logging_backend.repository.AuthRepository;
import com.example.logging_backend.util.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final AuthRepository authRepository;

    public AuthService(AuthenticationManager authenticationManager,
                       JwtUtil jwtUtil,
                       AuthRepository authRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.authRepository = authRepository;
    }

    public String loginAndGenerateToken(String username, String password) {
        System.out.println(username + " " + password);

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );

            Auth user = authRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));

            return jwtUtil.generateToken(user.getUsername());
        } catch (AuthenticationException e) {
            throw new RuntimeException("Kullanıcı adı veya şifre hatalı");
        }
    }



    public List<String> getAllUsersEmails() {
        return authRepository.findAllEmails();
    }

    public Auth findByUsername(String username) {
        return authRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Kullanıcı bulunamadı: " + username));
    }


}
