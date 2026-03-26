package edu.hitsz.Dao;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Record {
    private String username;
    private int score;
    private String date;
    private String time;

    public Record(String username, int score, String date, String time) {
        this.username = username;
        this.score = score;
        this.date = date;
        this.time = time;
    }

    public Record(String username, int score) {
        this.username = username;
        this.score = score;
        LocalDateTime now = LocalDateTime.now();
        this.date = now.toLocalDate().toString();
        this.time = now.toLocalTime().withNano(0).toString();
    }

    public String getUsername() { return username; }
    public int getScore() { return score; }
    public String getDate() { return date; }
    public String getTime() { return time; }

    @Override
    public String toString() {
        return username + "," + score + "," + date + "," + time;
    }
}