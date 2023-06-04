package com.example.chronos_hm40;

public class Course {
    private String title;
    private String subTitle;
    private int color;
    private String frequency;
    private String hourBegin;
    private String hourEnd;
    private String dateBegin;
    private String dateEnd;

    public Course(String title, String subTitle, int color, String frequency, String hourBegin, String hourEnd, String dateBegin, String dateEnd) {
        this.title = title;
        this.subTitle = subTitle;
        this.color = color;
        this.frequency = frequency;
        this.hourBegin = hourBegin;
        this.hourEnd = hourEnd;
        this.dateBegin = dateBegin;
        this.dateEnd = dateEnd;
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
}
