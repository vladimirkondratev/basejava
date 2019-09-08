package ru.javawebinar.basejava.model;

import java.util.List;

public class DateSection extends Section {
    private List<DateItem> listItem;

    public DateSection(String title, List<DateItem> listItem) {
        this.title = title;
        this.listItem = listItem;
    }

    public List<DateItem> getListItem() {
        return listItem;
    }

    public void setListItem(List<DateItem> listItem) {
        this.listItem = listItem;
    }

    @Override
    public String toString() {
        return title + "\n" + listItem;
    }
}
