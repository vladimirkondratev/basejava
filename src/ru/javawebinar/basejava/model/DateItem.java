package ru.javawebinar.basejava.model;

import java.time.LocalDate;
import java.util.Objects;

public class DateItem {
    private Link homepage;
    private LocalDate startDate;
    private LocalDate endDate;
    private String title;
    private String description;

    public DateItem(String name, String url, LocalDate startDate, LocalDate endDate, String title, String description) {
        Objects.requireNonNull(startDate, "start time must not be null");
        Objects.requireNonNull(endDate, "end time must not be null");
        Objects.requireNonNull(title, "title must not be null");
        this.homepage = new Link(name, url);
        this.startDate = startDate;
        this.endDate = endDate;
        this.title = title;
        this.description = description;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
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
        return "DateItem{" +
                "homepage=" + homepage +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DateItem dateItem = (DateItem) o;
        return startDate.equals(dateItem.startDate) &&
                endDate.equals(dateItem.endDate) &&
                title.equals(dateItem.title) &&
                description.equals(dateItem.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(startDate, endDate, title, description);
    }
}
