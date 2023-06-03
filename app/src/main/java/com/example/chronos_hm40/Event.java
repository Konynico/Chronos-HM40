package com.example.chronos_hm40;

public class Event {
    private int id;
    private String title;
    private String description;
    private int year;
    private int month;
    private int dayOfMonth;

    public Event(String title, String description, int year, int month, int dayOfMonth) {
        this.title = title;
        this.description = description;
        this.year = year;
        this.month = month;
        this.dayOfMonth = dayOfMonth;
    }

    public Event(int id, String title, String description, int year, int month, int dayOfMonth) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.year = year;
        this.month = month;
        this.dayOfMonth = dayOfMonth;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDayOfMonth() {
        return dayOfMonth;
    }
}

