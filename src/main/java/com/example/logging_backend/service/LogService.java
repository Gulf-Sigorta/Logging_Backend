package com.example.logging_backend.service;

import com.example.logging_backend.model.Log;
import com.example.logging_backend.repository.LogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LogService {

    @Autowired
    private LogRepository logRepository;

    public List<Log> getAllLogs() {
        return logRepository.findAllByOrderByTimestampDesc();
    }

    public List<Log> getLogByLevel(String level) {
        return logRepository.findByLevelOrderByTimestampDesc(level);
    }

}