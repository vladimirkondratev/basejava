package ru.javawebinar.basejava.model;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class OrganizationSection extends AbstractSection {
    private static final long serialVersionUID = 1L;

    private List<Organization> items;

    public OrganizationSection(String title, Organization... organizations) {
        this(title, Arrays.asList(organizations));
    }

    public OrganizationSection(String title, List<Organization> items) {
        Objects.requireNonNull(title, "title must not be null");
        Objects.requireNonNull(items, "list must not be null");
        this.title = title;
        this.items = items;
    }

    public List<Organization> getItems() {
        return items;
    }

    public void setItems(List<Organization> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return "DateSection{" +
                "title=" + title
                + items + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrganizationSection that = (OrganizationSection) o;
        return items.equals(that.items);
    }

    @Override
    public int hashCode() {
        return Objects.hash(items);
    }
}
