package com.example.logging_backend.controller;

import com.example.logging_backend.model.Log.Log;
import com.example.logging_backend.model.Log.LogLevelCount;
import com.example.logging_backend.service.LogService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/log")
public class LogController {

    @Autowired
    private LogService logService;

    @GetMapping
    public Page<Log> getAllLogs(Pageable page) {
        return logService.getAllLogs(page);
    }

    @GetMapping("/get-log-by-level/{level}")
    public Page<Log> getLogByLevel(@PathVariable String level, Pageable pageable) {
        return logService.getLogsByLevel(level, pageable);
    }

    @GetMapping("/level-counts")
    public List<LogLevelCount> getLogCounts() {
        return logService.getLogCountsByLevel();
    }

    @GetMapping("/level-counts-today")
    public List<LogLevelCount> getTodayLogCounts() {
        return logService.getTodayLogCountsByLevel();
    }

    @GetMapping("/level-counts-from-date")
    public List<LogLevelCount> getLogCountsFromDate(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate
    ) {
        return logService.getLogCountsByLevelFromDate(startDate);
    }

    @GetMapping("/get-logs-today")
    public List<Log> getLogsFromToday() {
        return logService.getLogsFromToday();
    }

    // YENİ: Belirli bir tarihe göre logları getirme endpoint'i
    // URL: /api/log/by-date/{date}
    @GetMapping("/by-date/{date}")
    public Page<Log> getLogsByDate(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            Pageable pageable) {
        return logService.getLogsByDate(date, pageable);
    }

    // YENİ: Belirli bir tarih ve seviyeye göre logları getirme endpoint'i
    // URL: /api/log/by-date-and-level/{date}/{level}
    @GetMapping("/by-date-and-level/{date}/{level}")
    public Page<Log> getLogsByDateAndLevel(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @PathVariable String level,
            Pageable pageable) {
        return logService.getLogsByDateAndLevel(date, level, pageable);
    }
}
