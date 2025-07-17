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
import java.time.YearMonth;
import java.time.ZonedDateTime;
import java.util.List;

@Service
public class LogService {


    @Autowired
    private LogRepository logRepository;

    public Page<Log> getAllLogs(Pageable pageable) {
        return logRepository.findAllByOrderByTimestampDesc(pageable);
    }

    public Page<Log> getLogsByLevel(String level, Pageable pageable) {
        return logRepository.findByLevelOrderByTimestampDesc(level, pageable);
    }

    public List<LogLevelCount> getLogCountsByLevel() {
        return logRepository.countLogsByLevel();
    }

    public List<LogLevelCount> getTodayLogCountsByLevel() {
        return logRepository.countTodayLogsByLevel();
    }

    public List<LogLevelCount> getLogCountsByLevelFromDate(LocalDate startDate) {
        LocalDateTime startDateTime = startDate.atStartOfDay();

        LocalDateTime endDateTime = startDateTime.plusMonths(1).minusSeconds(1);

        System.out.println("Start: " + startDateTime + ", End: " + endDateTime);

        return logRepository.countLogsByLevelFromDate(startDateTime, endDateTime);
    }


    public List<Log> getLogsFromToday() {
        return logRepository.findAllTodayLogs();
    }

}