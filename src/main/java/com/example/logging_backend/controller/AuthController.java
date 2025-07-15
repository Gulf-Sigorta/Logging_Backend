package com.example.logging_backend.controller;

import com.example.logging_backend.model.Auth.AuthRequest;
import com.example.logging_backend.model.Auth.UserResponse;
import com.example.logging_backend.service.AuthService;
import com.example.logging_backend.util.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request, HttpServletResponse response) {
        try {


            String token = authService.loginAndGenerateToken(request.getUsername(), request.getPassword());

            // Cookie oluştur
            Cookie cookie = new Cookie("jwt", token);
            cookie.setHttpOnly(true);
            cookie.setSecure(false);
            cookie.setPath("/");
            cookie.setMaxAge(60*60);

            response.addCookie(cookie);
            System.out.println("[GİRİŞ BAŞARILI] Token üretildi: " + token);
            return ResponseEntity.ok("Giriş başarılı");
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Geçersiz bilgiler");
        }
    }

    @GetMapping("/check")
    public ResponseEntity<?> checkAuth(@CookieValue(name = "jwt", required = false) String token) {
        if (token == null || token.isEmpty()) {
            return ResponseEntity.ok(new UserResponse(false)); // auth değil
        }

        boolean valid = jwtUtil.validateToken(token);

        return ResponseEntity.ok(new UserResponse(valid));
    }


    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("jwt", null);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(60*60);

        response.addCookie(cookie);

        return ResponseEntity.ok("Çıkış yapıldı");
    }
}

