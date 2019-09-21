package ru.javawebinar.basejava.storage.serializer;

import ru.javawebinar.basejava.model.*;

import java.io.*;
import java.time.LocalDate;
import java.util.*;

public class DataStreamIOStrategyImpl implements IOStrategy {

    @Override
    public void doWrite(Resume resume, OutputStream os) throws IOException {
        try (DataOutputStream dos = new DataOutputStream(os)) {
            dos.writeUTF(resume.getUuid());
            dos.writeUTF(resume.getFullName());
            Map<ContactType, String> contacts = resume.getContacts();
            writeCollection(contacts.entrySet(), dos, entry -> {
                dos.writeUTF(entry.getKey().name());
                dos.writeUTF(entry.getValue());
            });
            Map<SectionType, AbstractSection> sections = resume.getSections();
            writeCollection(sections.entrySet(), dos, entry -> {
                SectionType key = entry.getKey();
                AbstractSection section = entry.getValue();
                dos.writeUTF(key.name());
                switch (key) {
                    case OBJECTIVE:
                    case PERSONAL: {
                        dos.writeUTF(((TextSection) section).getText());
                        break;
                    }
                    case ACHIEVEMENT:
                    case QUALIFICATIONS: {
                        List<String> list = ((ListSection) section).getItems();
                        writeCollection(list, dos, dos::writeUTF);
                        break;
                    }
                    case EXPERIENCE:
                    case EDUCATION: {
                        List<Organization> organizationList = ((OrganizationSection) section).getItems();
                        writeCollection(organizationList, dos, organization -> {
                            dos.writeUTF(organization.getHomepage().getName());
                            writeStringNullable(dos, organization.getHomepage().getUrl());
                            List<Organization.Position> positionList = organization.getPositions();
                            writeCollection(positionList, dos, position -> {
                                dos.writeUTF(position.getStartDate().toString());
                                dos.writeUTF(position.getEndDate().toString());
                                dos.writeUTF(position.getTitle());
                                writeStringNullable(dos, position.getDescription());
                            });
                        });
                        break;
                    }
                }
            });
        }
    }

    private void writeStringNullable(DataOutputStream dos, String data) throws IOException {
        if (data != null) {
            dos.writeUTF(data);
        } else {
            dos.writeUTF("");
        }
    }

    private <T> void writeCollection(Collection<T> collection, DataOutputStream dos, Writer<T> action) throws IOException {
        dos.writeInt(collection.size());
        for (T t : collection) {
            action.write(t);
        }
    }

    @FunctionalInterface
    interface Writer<T> {
        void write(T t) throws IOException;
    }

    @Override
    public Resume doRead(InputStream is) throws IOException {
        try (DataInputStream dis = new DataInputStream(is)) {
            String uuid = dis.readUTF();
            String fullName = dis.readUTF();
            Resume resume = new Resume(uuid, fullName);

            Map<ContactType, String> contacts = new EnumMap<>(ContactType.class);
            readMap(contacts, dis, () -> new EnumMap.SimpleEntry<>(ContactType.valueOf(dis.readUTF()), dis.readUTF()));
            resume.setContacts(contacts);

            Map<SectionType, AbstractSection> sections = new EnumMap<>(SectionType.class);
            readMap(sections, dis, () -> {
                SectionType type = SectionType.valueOf(dis.readUTF());
                switch (type) {
                    case OBJECTIVE:
                    case PERSONAL: {
                        return new EnumMap.SimpleEntry<>(type, new TextSection(dis.readUTF()));
                    }
                    case ACHIEVEMENT:
                    case QUALIFICATIONS: {
                        List<String> sectionList = new ArrayList<>();
                        readCollection(sectionList, dis, dis::readUTF);
                        return new EnumMap.SimpleEntry<>(type, new ListSection(sectionList));
                    }
                    case EXPERIENCE:
                    case EDUCATION: {
                        List<Organization> organizationList = new ArrayList<>();
                        readCollection(organizationList, dis, () -> {
                            String organizationName = dis.readUTF();
                            String url = readStringNullable(dis);
                            Link homepage = new Link(organizationName, url);
                            List<Organization.Position> positionList = new ArrayList<>();
                            readCollection(positionList, dis, () -> {
                                LocalDate startDay = LocalDate.parse(dis.readUTF());
                                LocalDate endDay = LocalDate.parse(dis.readUTF());
                                String title = dis.readUTF();
                                String description = readStringNullable(dis);
                                return new Organization.Position(startDay, endDay, title, description);
                            });
                            return new Organization(homepage, positionList);
                        });
                        return new EnumMap.SimpleEntry<>(type, new OrganizationSection(organizationList));
                    }
                }
                return null;
            });
            resume.setSections(sections);
            return resume;
        }
    }

    private String readStringNullable(DataInputStream dis) throws IOException {
        String string = dis.readUTF();
        return string.equals("") ? null : string;
    }

    @FunctionalInterface
    interface CollectionReader<T> {
        T read() throws IOException;
    }

    @FunctionalInterface
    interface MapReader<K, V> {
        Map.Entry<K, V> read() throws IOException;
    }

    private <T> void readCollection(Collection<T> collection, DataInputStream dis, CollectionReader<T> action) throws IOException {
        int size = dis.readInt();
        for (int i = 0; i < size; i++) {
            collection.add(action.read());
        }
    }

    private <K, V> void readMap(Map<K, V> map, DataInputStream dis, MapReader<K, V> action) throws IOException {
        int size = dis.readInt();
        for (int i = 0; i < size; i++) {
            Map.Entry<K, V> mapEntry = action.read();
            map.put(mapEntry.getKey(), mapEntry.getValue());
        }
    }
}