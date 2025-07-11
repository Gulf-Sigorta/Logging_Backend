package com.example.logging_backend.controller;

import com.example.logging_backend.model.Log;
import com.example.logging_backend.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/log")
public class LogController {

    @Autowired
    private LogService logService;

    @GetMapping
    public List<Log> tumLoglariGetir() {
        return logService.getAllLogs();
    }

    @GetMapping ("/get-log-by-level")
    public List<Log> getLogByLevel(@RequestParam String level) {
        return logService.getLogByLevel(level);
    }
}