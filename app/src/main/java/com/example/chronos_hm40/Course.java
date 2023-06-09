package com.example.chronos_hm40;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "courses")
public class Course implements Comparable<Course> {
    @PrimaryKey(autoGenerate = true)
    private Integer id;

    private String title;
    private String subTitle;
    private int color;

    private String day;
    private String frequency;
    private String hourBegin;
    private String hourEnd;
    private String dateBegin;
    private String dateEnd;

    public Course(String title, String subTitle, int color,String day, String frequency, String hourBegin, String hourEnd, String dateBegin, String dateEnd) {
        this.title = title;
        this.subTitle = subTitle;
        this.color = color;
        this.day = day;
        this.frequency = frequency;
        this.hourBegin = hourBegin;
        this.hourEnd = hourEnd;
        this.dateBegin = dateBegin;
        this.dateEnd = dateEnd;
    }

    @Override
    public int compareTo(Course otherCourse) {
        // Comparez les horaires des cours
        // Utilisez les horaires de début pour le tri
        return this.getHourBegin().compareTo(otherCourse.getHourBegin());
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getHourBegin() {
        return hourBegin;
    }

    public void setHourBegin(String hourBegin) {
        this.hourBegin = hourBegin;
    }

    public String getHourEnd() {
        return hourEnd;
    }

    public void setHourEnd(String hourEnd) {
        this.hourEnd = hourEnd;
    }

    public String getDateBegin() {
        return dateBegin;
    }

    public void setDateBegin(String dateBegin) {
        this.dateBegin = dateBegin;
    }

    public String getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(String dateEnd) {
        this.dateEnd = dateEnd;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }
}
