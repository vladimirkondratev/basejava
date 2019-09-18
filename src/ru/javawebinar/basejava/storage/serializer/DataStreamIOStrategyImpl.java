package ru.javawebinar.basejava.storage.serializer;

import ru.javawebinar.basejava.model.*;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DataStreamIOStrategyImpl implements IOStrategy {

    @Override
    public void doWrite(Resume resume, OutputStream os) throws IOException {
        try (DataOutputStream dos = new DataOutputStream(os)) {
            dos.writeUTF(resume.getUuid());
            dos.writeUTF(resume.getFullName());
            Map<ContactType, String> contacts = resume.getContacts();
            dos.writeInt(contacts.size());
            for (Map.Entry<ContactType, String> entry : contacts.entrySet()) {
                dos.writeUTF(entry.getKey().name());
                dos.writeUTF(entry.getValue());
            }

            Map<SectionType, AbstractSection> sections = resume.getSections();
            dos.writeInt(sections.size());
            for (Map.Entry<SectionType, AbstractSection> entry : sections.entrySet()) {
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
                        int size = list.size();
                        dos.writeInt(size);
                        for (int i = 0; i < size; i++) {
                            dos.writeUTF(list.get(i));
                        }
                        break;
                    }
                    case EXPERIENCE:
                    case EDUCATION: {
                        List<Organization> organizationList = ((OrganizationSection) section).getItems();
                        int organizationListSize = organizationList.size();
                        dos.writeInt(organizationListSize);
                        for (int i = 0; i < organizationListSize; i++) {
                            Organization organization = organizationList.get(i);
                            dos.writeUTF(organization.getHomepage().getName());
                            dos.writeUTF(organization.getHomepage().getUrl());
                            List<Organization.Position> positionList = organization.getPositions();
                            int positionListSize = positionList.size();
                            dos.writeInt(positionListSize);
                            for (int j = 0; j < positionListSize; j++) {
                                Organization.Position position = positionList.get(j);
                                dos.writeUTF(position.getStartDate().toString());
                                dos.writeUTF(position.getEndDate().toString());
                                dos.writeUTF(position.getTitle());
                                String description = position.getDescription();
                                if (description != null) {
                                    dos.writeBoolean(true);
                                    dos.writeUTF(description);
                                } else {
                                    dos.writeBoolean(false);
                                }
                            }
                        }
                        break;
                    }
                }
            }
        }
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
                            Link homepage = new Link(dis.readUTF(), dis.readUTF());
                            List<Organization.Position> positionList = new ArrayList<>();
                            int positionListSize = dis.readInt();
                            for (int k = 0; k < positionListSize; k++) {
                                LocalDate startDay = LocalDate.parse(dis.readUTF());
                                LocalDate endDay = LocalDate.parse(dis.readUTF());
                                String title = dis.readUTF();
                                String description = dis.readBoolean() ? dis.readUTF() : null;
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