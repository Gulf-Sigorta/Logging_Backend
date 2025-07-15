package com.example.logging_backend.model.Log;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@Entity
@Table(name = "application_logs")
public class Log {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "log_level")
    private String level;

    @Column(name = "message")
    private String message;

    @Column(name = "log_timestamp")
    private Timestamp timestamp;

    @Column(name = "class_name")
    private String source;  // Log kaynağı (logger)

    @Column(name = "thread_name")
    private String thread;  // Thread bilgisi
}
