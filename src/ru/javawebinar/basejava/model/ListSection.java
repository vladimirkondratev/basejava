package ru.javawebinar.basejava.model;

import java.util.List;

public class ListSection extends Section {
    private List<String> listItem;

    public ListSection(String title, List<String> listItem) {
        this.title = title;
        this.listItem = listItem;
    }

    public List<String> getListItem() {
        return listItem;
    }

    public void setListItem(List<String> listItem) {
        this.listItem = listItem;
    }

    @Override
    public String toString() {
        return title + "\n" + listItem;
    }
}
