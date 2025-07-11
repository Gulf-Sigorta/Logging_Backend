package com.example.logging_backend.service;

import com.example.logging_backend.model.Log;
import com.example.logging_backend.repository.LogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LogService {

    @Autowired
    private LogRepository logRepository;

    public Page<Log> getAllLogs(Pageable pageable) {
        return logRepository.findAllByOrderByTimestampDesc(pageable);
    }

    public Page<Log> getLogsByLevel(String level,Pageable pageable) {
        return logRepository.findByLevelOrderByTimestampDesc(level, pageable);
    }

}