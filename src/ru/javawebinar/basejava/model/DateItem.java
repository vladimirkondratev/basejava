package ru.javawebinar.basejava.model;

import java.time.LocalDate;
import java.util.Objects;

public class DateItem {
    private LocalDate startTime;
    private LocalDate endTime;
    private String title;
    private String description;

    public DateItem(LocalDate startTime, LocalDate endTime, String title, String description) {
        Objects.requireNonNull(startTime, "start time must not be null");
        Objects.requireNonNull(endTime, "end time must not be null");
        Objects.requireNonNull(title, "title must not be null");
        Objects.requireNonNull(description, "description must not be null");
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DateItem dateItem = (DateItem) o;
        return startTime.equals(dateItem.startTime) &&
                endTime.equals(dateItem.endTime) &&
                title.equals(dateItem.title) &&
                description.equals(dateItem.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(startTime, endTime, title, description);
    }
}
