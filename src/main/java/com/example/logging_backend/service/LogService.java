package com.example.logging_backend.service;

import com.example.logging_backend.model.Log.Log;
import com.example.logging_backend.model.Log.LogLevelCount;
import com.example.logging_backend.repository.LogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class LogService {


    @Autowired
    private LogRepository logRepository;

    public Page<Log> getAllLogs(Pageable pageable) {
        try {
            return logRepository.findAllByOrderByTimestampDesc(pageable);
        } catch (Exception e) {
            throw new RuntimeException("Log sayıları alınırken hata oluştu", e);
        }
    }

    public Page<Log> getLogsByLevel(String level, Pageable pageable) {
        try {
            return logRepository.findByLevelOrderByTimestampDesc(level, pageable);
        } catch (Exception e) {
            throw new RuntimeException("Loglar seviyeye göre getirilirken hata oluştu", e);
        }
    }

    public List<LogLevelCount> getLogCountsByLevel() {
        try {
            return logRepository.countLogsByLevel();
        } catch (Exception e) {
            throw new RuntimeException("Seviyelere göre log sayıları alınırken hata oluştu", e);
        }
    }

    public List<LogLevelCount> getTodayLogCountsByLevel() {
        try {
            return logRepository.countTodayLogsByLevel();
        } catch (Exception e) {
            throw new RuntimeException("Bugünün log sayıları alınırken hata oluştu", e);
        }
    }

    public List<LogLevelCount> getLogCountsByLevelFromDate(LocalDate startDate) {
        try {
            LocalDateTime startDateTime = startDate.atStartOfDay();
            LocalDateTime endDateTime = startDateTime.plusMonths(1).minusSeconds(1);


            return logRepository.countLogsByLevelFromDate(startDateTime, endDateTime);
        } catch (Exception e) {
            throw new RuntimeException("Belirtilen tarihten itibaren log sayıları alınırken hata oluştu", e);
        }
    }

    public List<Log> getLogsFromToday() {
        try {
            return logRepository.findAllTodayLogs();
        } catch (Exception e) {
            throw new RuntimeException("Bugünün logları alınırken hata oluştu", e);
        }
    }

}