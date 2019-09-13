package ru.javawebinar.basejava.model;

import java.io.Serializable;
import java.util.Objects;

public class Contact implements Serializable {
    private static final long serialVersionUID = 1L;

    private String value;

    public Contact(String value) {
        Objects.requireNonNull(value, "value must not be null");
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Contact contact = (Contact) o;
        return value.equals(contact.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
