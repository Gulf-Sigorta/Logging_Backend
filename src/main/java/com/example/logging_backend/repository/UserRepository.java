package com.example.logging_backend.repository;

import com.example.logging_backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User,Long> {

    List<User> findByUsername(String username);
    User findById(long id);
}
