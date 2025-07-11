package com.example.logging_backend.model;

import jakarta.persistence.*;
import java.sql.Timestamp;
@Entity
@Table(name= "logs" )
public class DenemeLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private long id;
    private String level;
    private String message;
    private Timestamp timestamp;
    private String source;

    public DenemeLog() {}
    public long getId() {
        return id;

    }
    public void setId(long id) {
        this.id = id;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;

    }

    public void setMessage(String message) {
        this.message = message;
    }
    public String getLevel() {
        return level;
    }
    public void setLevel(String level) {
        this.level=level;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source=source;
    }
}
