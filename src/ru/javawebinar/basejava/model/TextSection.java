package ru.javawebinar.basejava.model;

public class TextSection extends Section {
    private String text;

    public TextSection(String title, String text) {
        this.title = title;
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return title + "\n  " + text;
    }
}
