package ru.javawebinar.basejava.model;

import java.util.List;
import java.util.Objects;

public class Organization {
    private Link homepage;
    private List<Period> periods;

    public Organization(String name, String url, List<Period> periods) {
        this.homepage = new Link(name, url);
        this.periods = periods;
    }

    public List<Period> getPeriods() {
        return periods;
    }

    public void setPeriods(List<Period> periods) {
        this.periods = periods;
    }

    @Override
    public String toString() {
        return "DateItem{" +
                "homepage=" + homepage +
                ", periods=" + periods +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Organization organization = (Organization) o;
        return Objects.equals(homepage, organization.homepage) &&
                Objects.equals(periods, organization.periods);
    }

    @Override
    public int hashCode() {
        return Objects.hash(homepage, periods);
    }
}
