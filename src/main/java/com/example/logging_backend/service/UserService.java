package com.example.logging_backend.service;

import com.example.logging_backend.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;


@Service
public class UserService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    public UserService(AuthenticationManager authenticationManager,
                       JwtService jwtService,
                       UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    public String loginAndGenerateToken(String username, String password) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );

            var user = userRepository.findByUsername(username);
            if (user == null) {
                throw new RuntimeException("Kullanıcı bulunamadı");
            }

            return jwtService.generateToken(user.getUsername());
        } catch (AuthenticationException e) {
            throw new RuntimeException("Kullanıcı adı veya şifre hatalı");
        }
    }
}
