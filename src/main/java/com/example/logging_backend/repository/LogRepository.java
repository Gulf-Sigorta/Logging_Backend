package com.example.logging_backend.repository;

import com.example.logging_backend.model.Log.Log;
import com.example.logging_backend.model.Log.LogLevelCount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface LogRepository extends JpaRepository<Log,Long> {
    List<Log> findByLevel (String level) ;

    Page<Log> findAllByOrderByTimestampDesc(Pageable pageable);
    Page<Log> findByLevelOrderByTimestampDesc(String level, Pageable pageable);

    @Query("SELECT l.level AS level, COUNT(l) AS count FROM Log l GROUP BY l.level")
    List<LogLevelCount> countLogsByLevel();

    @Query("SELECT l.level AS level, COUNT(l) AS count " +
            "FROM Log l " +
            "WHERE l.timestamp >= CURRENT_DATE " +
            "GROUP BY l.level")
    List<LogLevelCount> countTodayLogsByLevel();


    @Query("SELECT l FROM Log l WHERE l.timestamp >= CURRENT_DATE ORDER BY l.timestamp DESC")
    List<Log> findAllTodayLogs();


    @Query("SELECT l.level AS level, COUNT(l) AS count " +
            "FROM Log l " +
            "WHERE l.timestamp >= :startDate AND l.timestamp < :endDate " +
            "GROUP BY l.level")
    List<LogLevelCount> countLogsByLevelFromDate(LocalDateTime startDate,LocalDateTime endDate);

    @Query("SELECT COUNT(l) FROM Log l WHERE l.timestamp >= :since")
    long countAllLogsSince(@Param("since") Timestamp since);

    @Query("SELECT COUNT(l) FROM Log l WHERE l.level = 'ERROR' AND l.timestamp >= :since")
    long countErrorLogsSince(@Param("since") Timestamp since);
}
