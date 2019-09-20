package ru.javawebinar.basejava.storage.serializer;

import ru.javawebinar.basejava.model.*;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

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
            action.write((T) t);
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
            int size = dis.readInt();
            for (int i = 0; i < size; i++) {
                resume.addContact(ContactType.valueOf(dis.readUTF()), dis.readUTF());
            }
            int sectionsSize = dis.readInt();
            for (int i = 0; i < sectionsSize; i++) {
                SectionType type = SectionType.valueOf(dis.readUTF());
                switch (type) {
                    case OBJECTIVE:
                    case PERSONAL: {
                        resume.addSection(type,
                                new TextSection(dis.readUTF()));
                        break;
                    }
                    case ACHIEVEMENT:
                    case QUALIFICATIONS: {
                        int sizeListSection = dis.readInt();
                        List<String> sectionList = new ArrayList<>();
                        for (int j = 0; j < sizeListSection; j++) {
                            sectionList.add(dis.readUTF());
                        }
                        resume.addSection(type, new ListSection(sectionList));
                        break;
                    }
                    case EXPERIENCE:
                    case EDUCATION: {
                        int organizationListSize = dis.readInt();
                        List<Organization> organizationList = new ArrayList<>();
                        for (int j = 0; j < organizationListSize; j++) {
                            String organizationName = dis.readUTF();
                            String urlTemp = dis.readUTF();
                            String url = urlTemp.equals("") ? null : urlTemp;
                            Link homepage = new Link(organizationName, url);
                            List<Organization.Position> positionList = new ArrayList<>();
                            int positionListSize = dis.readInt();
                            for (int k = 0; k < positionListSize; k++) {
                                LocalDate startDay = LocalDate.parse(dis.readUTF());
                                LocalDate endDay = LocalDate.parse(dis.readUTF());
                                String title = dis.readUTF();
                                String descriptionTemp = dis.readUTF();
                                String description = descriptionTemp.equals("") ? null : descriptionTemp;
                                positionList.add(new Organization.Position(startDay, endDay, title, description));
                            }
                            organizationList.add(new Organization(homepage, positionList));
                        }
                        resume.addSection(type, new OrganizationSection(organizationList));
                        break;
                    }
                }
            }
            return resume;
        }
    }
}