package ru.javawebinar.basejava.model;

import java.util.*;

/**
 * ru.javawebinar.basejava.model.Resume class
 */
public class Resume implements Comparable {

    // Unique identifier
    private final String uuid;

    private final String fullName;

    private Map<ContactType, Contact> contacts = new HashMap<>();

    private Map<SectionType, Section> sections = new HashMap<>();

    public Resume(String fullName) {
        this(UUID.randomUUID().toString(), fullName);
    }

    public Resume(String uuid, String fullName) {
        Objects.requireNonNull(uuid, "uuid must not be null");
        Objects.requireNonNull(fullName, "fullName must not be null");
        this.uuid = uuid;
        this.fullName = fullName;
    }

    public String getUuid() {
        return uuid;
    }

    public String getFullName() {
        return fullName;
    }

    public Contact getContact(ContactType type) {
        return contacts.get(type);
    }

    public void putContact(ContactType type, String value) {
        contacts.put(type, new Contact(value));
    }

    public Section getSection(SectionType type) {
        return sections.get(type);
    }

    public void putSection(SectionType type, Section section) {
        sections.put(type, section);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Resume resume = (Resume) o;
        return uuid.equals(resume.uuid) &&
                fullName.equals(resume.fullName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, fullName);
    }

    @Override
    public String toString() {
        return uuid + " " + fullName;
    }

    @Override
    public int compareTo(Object o) {
        return Comparator.comparing(Resume::getFullName)
                .thenComparing(Resume::getUuid)
                .compare(this, (Resume) o);
    }
}