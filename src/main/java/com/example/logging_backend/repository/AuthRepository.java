package com.example.logging_backend.repository;

import com.example.logging_backend.model.Auth.Auth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.Optional;
import java.util.List;

public interface AuthRepository extends JpaRepository<Auth,Long> {

    Optional<Auth> findByUsername(String username);

    @Query("SELECT a.email FROM Auth a")
    List<String> findAllEmails();

}
