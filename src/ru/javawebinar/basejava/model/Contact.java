package ru.javawebinar.basejava.model;

public class Contact {
    private String value;

    public Contact() {
    }

    public Contact(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
