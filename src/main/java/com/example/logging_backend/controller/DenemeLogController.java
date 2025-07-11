package com.example.logging_backend.controller;

import com.example.logging_backend.model.DenemeLog;
import com.example.logging_backend.service.DenemeLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/loglar")
public class DenemeLogController {

    @Autowired
    private DenemeLogService logService;

    @GetMapping
    public List<DenemeLog> tumLoglariGetir() {
        return logService.getAllLogs();
    }
}