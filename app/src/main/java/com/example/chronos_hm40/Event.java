package com.example.chronos_hm40;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

public class Event {
    private String title;
    private String description;
    private int color;
    private String date;

    public Event(String title, String description, int color, String date) {
        this.title = title;
        this.description = description;
        this.color = color;
        this.date = date;
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

