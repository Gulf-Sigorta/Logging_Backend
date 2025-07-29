package com.example.logging_backend.controller;

import com.example.logging_backend.model.Auth.AuthRequest;
import com.example.logging_backend.model.Auth.AuthResponse;
import com.example.logging_backend.service.AuthService;
import com.example.logging_backend.util.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request,
                                   HttpServletResponse response) {
        try {
            String token = authService.loginAndGenerateToken(request.getUsername(), request.getPassword());

            Cookie cookie = new Cookie("jwt", token);
            cookie.setHttpOnly(true);
            cookie.setSecure(false); // üretim ortamında true olmalı
            cookie.setPath("/");
            cookie.setMaxAge(60 * 60);
            response.addCookie(cookie);
            AuthResponse authResponse = new AuthResponse(true, token);

            return ResponseEntity.ok(authResponse);

        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Geçersiz bilgiler");
        }
    }

    @GetMapping("/check")
    public ResponseEntity<?> checkAuth(
            @CookieValue(name = "jwt", required = false) String cookieToken,
            @RequestHeader(name = "Authorization", required = false) String authHeader) {

        String token = null;

        // Token'ı cookie'den veya header'dan al
        if (cookieToken != null && !cookieToken.isEmpty()) {
            token = cookieToken;
        } else if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7); // "Bearer " sonrasını al
        }

        if (token == null || token.isEmpty()) {
            return ResponseEntity.ok(new AuthResponse(false, null));
        }

        boolean valid = jwtUtil.validateToken(token);
        return ResponseEntity.ok(new AuthResponse(valid, valid ? token : null));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        // Web için: Cookie'yi sil
        Cookie cookie = new Cookie("jwt", null);
        cookie.setHttpOnly(true);
        cookie.setSecure(false); // üretim ortamında true olmalı
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        return ResponseEntity.ok("Çıkış yapıldı");
    }
}
