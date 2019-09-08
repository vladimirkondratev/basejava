package ru.javawebinar.basejava.model;

public enum ContactType {
    PHONE("Phone"),
    EMAIL("e-mail"),
    SKYPE("Skype");

    private String title;

    ContactType(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
