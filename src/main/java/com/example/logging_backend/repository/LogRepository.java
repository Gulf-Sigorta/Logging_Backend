package com.example.logging_backend.repository;

import com.example.logging_backend.model.Log;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LogRepository extends JpaRepository<Log,Long> {
    List<Log> findAllByOrderByTimestampDesc();
    List<Log> findByLevel (String level) ;

    List<Log> findByLevelOrderByTimestampDesc(String level);


}
