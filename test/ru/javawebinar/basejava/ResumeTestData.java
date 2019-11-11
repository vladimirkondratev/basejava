package ru.javawebinar.basejava;

import ru.javawebinar.basejava.model.*;
import ru.javawebinar.basejava.util.DateUtil;

import java.time.Month;
import java.util.ArrayList;
import java.util.List;

public class ResumeTestData {

    public static Resume getResume(String uuid, String fullName) {
        Resume resume = new Resume(uuid, fullName);

        switch (uuid) {
            case "uuid1": {
                resume.addContact(ContactType.PHONE, "+7(900) 111-1111");
                resume.addContact(ContactType.MAIL, "account@yandex.ru");
                resume.addContact(ContactType.SKYPE, "skype.skype");

                resume.addSection(SectionType.OBJECTIVE, new TextSection("Ведущий стажировок и корпоративного обучения по Java Web и Enterprise технологиям."));
                resume.addSection(SectionType.PERSONAL, new TextSection("Аналитический склад ума, сильная логика, креативность, инициативность. Пурист кода и архитектуры."));

                List<String> achievementList = new ArrayList<>();
                achievementList.add("С 2013 года: разработка проектов \"Разработка Web приложения\",\"Java Enterprise\", \"Многомодульный maven. " +
                        "Многопоточность. XML (JAXB/StAX). Веб сервисы (JAX-RS/SOAP). Удаленное взаимодействие (JMS/AKKA)\". " +
                        "Организация онлайн стажировок и ведение проектов. Более 1000 выпускников.");
                achievementList.add("Реализация двухфакторной аутентификации для онлайн платформы управления проектами Wrike. Интеграция с Twilio, " +
                        "DuoSecurity, Google Authenticator, Jira, Zendesk.");
                achievementList.add("Налаживание процесса разработки и непрерывной интеграции ERP системы River BPM. Интеграция с 1С, Bonita BPM," +
                        "CMIS, LDAP. Разработка приложения управления окружением на стеке: Scala/Play/Anorm/JQuery. Разработка SSO" +
                        "аутентификации и авторизации различных ERP модулей, интеграция CIFS/SMB java сервера.");
                resume.addSection(SectionType.ACHIEVEMENT, new ListSection(achievementList));

                List<String> qualificationList = new ArrayList<>();
                qualificationList.add("JEE AS: GlassFish (v2.1, v3), OC4J, JBoss, Tomcat, Jetty, WebLogic, WSO2");
                qualificationList.add("Version control: Subversion, Git, Mercury, ClearCase, Perforce");
                qualificationList.add("Java Frameworks: Java 8 (Time API, Streams), Guava, Java Executor, MyBatis, Spring (MVC, Security, Data, Clouds, Boot), JPA " +
                        "(Hibernate, EclipseLink), Guice, GWT(SmartGWT, ExtGWT/GXT), Vaadin, Jasperreports, Apache Commons, Eclipse SWT, JUnit, " +
                        "Selenium (htmlelements).");
                resume.addSection(SectionType.QUALIFICATIONS, new ListSection(qualificationList));

                List<Organization.Position> periods1 = new ArrayList<>();
                periods1.add(new Organization.Position(DateUtil.of(2013, Month.OCTOBER),
                        DateUtil.NOW,
                        "Автор проекта.",
                        "Создание, организация и проведение Java онлайн проектов и стажировок."));
                Organization experience1 = new Organization(
                        new Link("Java Online Projects", "http://javaops.ru/"),
                        periods1);

                List<Organization.Position> periods2 = new ArrayList<>();
                periods2.add(new Organization.Position(DateUtil.of(2014, Month.OCTOBER),
                        DateUtil.of(2016, Month.JANUARY),
                        "Старший разработчик (backend)",
                        "Проектирование и разработка онлайн платформы управления проектами Wrike (Java 8 API, Maven, Spring, MyBatis,\n" +
                                "Guava, Vaadin, PostgreSQL, Redis). Двухфакторная аутентификация, авторизация по OAuth1, OAuth2, JWT SSO."));
                Organization experience2 = new Organization(
                        new Link("Wrike", "https://www.wrike.com/"),
                        periods2);

                List<Organization> experienceList = new ArrayList<>();
                experienceList.add(experience1);
                experienceList.add(experience2);
                resume.addSection(SectionType.EXPERIENCE, new OrganizationSection(experienceList));

                List<Organization.Position> periods3 = new ArrayList<>();
                periods3.add(new Organization.Position(DateUtil.of(2013, Month.MARCH),
                        DateUtil.of(2013, Month.MAY),
                        "Functional Programming Principles in Scala\" by Martin Odersky",
                        null));
                Organization education1 = new Organization(
                        new Link("Coursera", "https://www.coursera.org/course/progfun"),
                        periods3);

                List<Organization.Position> periods4 = new ArrayList<>();
                periods4.add(new Organization.Position(DateUtil.of(1993, Month.SEPTEMBER),
                        DateUtil.of(1996, Month.JULY),
                        "Аспирантура (программист С, С++)",
                        null));
                periods4.add(new Organization.Position(DateUtil.of(1987, Month.SEPTEMBER),
                        DateUtil.of(1993, Month.JULY),
                        "Инженер (программист Fortran, C)",
                        null));
                Organization education2 = new Organization(
                        new Link("Санкт-Петербургский национальный исследовательский университет информационных технологий, механики и оптики",
                                null),
                        periods4);
                List<Organization> educationList = new ArrayList<>();
                educationList.add(education1);
                educationList.add(education2);
                resume.addSection(SectionType.EDUCATION, new OrganizationSection(educationList));
                break;
            }
            case "uuid2":
              //  resume.addContact(ContactType.PHONE, "+71234567890");
                resume.addSection(SectionType.OBJECTIVE, new TextSection("Бездельник"));
                break;
            case "uuid3":
                resume.addContact(ContactType.MAIL, "name@domen.ru");
                break;
            case "uuid4":
               // resume.addContact(ContactType.SKYPE, "skype");
                break;
        }
        return resume;
    }

}
