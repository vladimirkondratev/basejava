package ru.javawebinar.basejava.model;

import java.util.List;
import java.util.Objects;

public class DateSection extends AbstractSection {
    private List<DateItem> items;

    public DateSection(String title, List<DateItem> items) {
        Objects.requireNonNull(title, "title must not be null");
        Objects.requireNonNull(items, "list must not be null");
        this.title = title;
        this.items = items;
    }

    public List<DateItem> getItems() {
        return items;
    }

    public void setItems(List<DateItem> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return title + "\n" + items;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DateSection that = (DateSection) o;
        return items.equals(that.items);
    }

    @Override
    public int hashCode() {
        return Objects.hash(items);
    }
}
