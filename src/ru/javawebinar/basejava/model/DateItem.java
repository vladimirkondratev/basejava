package ru.javawebinar.basejava.model;

import java.time.LocalDate;

public class DateItem {
    private LocalDate startTime;
    private LocalDate endTime;
    private String title;
    private String description;

    public DateItem(LocalDate startTime, LocalDate endTime, String title, String description) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.title = title;
        this.description = description;
    }

    public LocalDate getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDate startTime) {
        this.startTime = startTime;
    }

    public LocalDate getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDate endTime) {
        this.endTime = endTime;
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

    @Override
    public String toString() {
        return startTime + " - " + endTime + "   " + title + "/n  " + description;
    }
}
