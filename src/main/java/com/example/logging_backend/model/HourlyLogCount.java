package com.example.logging_backend.model;

public class HourlyLogCount {
    private String hour;  // "00:00", "01:00" gibi
    private long count;

    public HourlyLogCount(String hour, long count) {
        this.hour = hour;
        this.count = count;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }


}
