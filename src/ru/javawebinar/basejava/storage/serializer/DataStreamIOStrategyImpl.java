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

            readItems(dis, () -> resume.addContact(ContactType.valueOf(dis.readUTF()), dis.readUTF()));

            readItems(dis, () -> {
                SectionType type = SectionType.valueOf(dis.readUTF());
                switch (type) {
                    case OBJECTIVE:
                    case PERSONAL: {
                        resume.addSection(type, new TextSection(dis.readUTF()));
                        break;
                    }
                    case ACHIEVEMENT:
                    case QUALIFICATIONS: {
                        List<String> sectionList = readCollection(dis, dis::readUTF);
                        resume.addSection(type, new ListSection(sectionList));
                        break;
                    }
                    case EXPERIENCE:
                    case EDUCATION: {
                        List<Organization> organizationList = readCollection(dis, () -> {
                            String organizationName = dis.readUTF();
                            String url = readStringNullable(dis);
                            Link homepage = new Link(organizationName, url);
                            List<Organization.Position> positionList = readCollection(dis, () -> {
                                LocalDate startDay = LocalDate.parse(dis.readUTF());
                                LocalDate endDay = LocalDate.parse(dis.readUTF());
                                String title = dis.readUTF();
                                String description = readStringNullable(dis);
                                return new Organization.Position(startDay, endDay, title, description);
                            });
                            return new Organization(homepage, positionList);
                        });
                        resume.addSection(type, new OrganizationSection(organizationList));
                        break;
                    }
                }
            });
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
    interface ItemReader {
        void read() throws IOException;
    }

    private void readItems(DataInputStream dis, ItemReader action) throws IOException {
        int size = dis.readInt();
        for (int i = 0; i < size; i++) {
            action.read();
        }
    }

    private <T> List<T> readCollection(DataInputStream dis, CollectionReader<T> action) throws IOException {
        int size = dis.readInt();
        List<T> list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            list.add(action.read());
        }
        return list;
    }
}