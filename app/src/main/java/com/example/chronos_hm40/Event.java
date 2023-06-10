package com.example.chronos_hm40;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

public class Event {
    private String date;
    private String title;
    private String description;
    private int color;

    public Event(String date, String title, String description, int color) {
        this.date = date;
        this.title = title;
        this.description = description;
        this.color = color;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}

