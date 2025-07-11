package com.example.logging_backend.service;

import com.example.logging_backend.model.DenemeLog;
import com.example.logging_backend.repository.DenemeLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DenemeLogService {

    @Autowired
    private DenemeLogRepository logRepository;

    public List<DenemeLog> getAllLogs() {
        return logRepository.findAll();
    }
}