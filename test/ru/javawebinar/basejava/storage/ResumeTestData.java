package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ResumeTestData {

    private static final String UUID_1 = "uuid1";
    private static final String UUID_2 = "uuid2";
    private static final String UUID_3 = "uuid3";
    private static final String UUID_4 = "uuid4";

    static Resume resume1 = new Resume(UUID_1, "fullName1");
    static Resume resume2 = new Resume(UUID_2, "fullName2");
    static Resume resume3 = new Resume(UUID_3, "fullName3");
    static Resume resume4 = new Resume(UUID_4, "fullName4");

    static {
        resume1.addContact(ContactType.PHONE, "+7(900) 111-1111");
        resume1.addContact(ContactType.MAIL, "account@yandex.ru");
        resume1.addContact(ContactType.SKYPE, "skype.skype");

        resume1.addSection(SectionType.OBJECTIVE, new TextSection("Позиция", "Ведущий стажировок и корпоративного обучения по Java Web и Enterprise технологиям."));
        resume1.addSection(SectionType.PERSONAL, new TextSection("Личные качества", "Аналитический склад ума, сильная логика, креативность, инициативность. Пурист кода и архитектуры."));

        List<String> achievementList = new ArrayList<>();
        achievementList.add("С 2013 года: разработка проектов \"Разработка Web приложения\",\"Java Enterprise\", \"Многомодульный maven.\n" +
                "Многопоточность. XML (JAXB/StAX). Веб сервисы (JAX-RS/SOAP). Удаленное взаимодействие (JMS/AKKA)\".\n" +
                "Организация онлайн стажировок и ведение проектов. Более 1000 выпускников.");
        achievementList.add("Реализация двухфакторной аутентификации для онлайн платформы управления проектами Wrike. Интеграция с Twilio,\n" +
                "DuoSecurity, Google Authenticator, Jira, Zendesk.");
        achievementList.add("Налаживание процесса разработки и непрерывной интеграции ERP системы River BPM. Интеграция с 1С, Bonita BPM,\n" +
                "CMIS, LDAP. Разработка приложения управления окружением на стеке: Scala/Play/Anorm/JQuery. Разработка SSO\n" +
                "аутентификации и авторизации различных ERP модулей, интеграция CIFS/SMB java сервера.");
        resume1.addSection(SectionType.ACHIEVEMENT, new ListSection("Достижения", achievementList));

        List<String> qualificationList = new ArrayList<>();
        qualificationList.add("JEE AS: GlassFish (v2.1, v3), OC4J, JBoss, Tomcat, Jetty, WebLogic, WSO2");
        qualificationList.add("Version control: Subversion, Git, Mercury, ClearCase, Perforce");
        qualificationList.add("Java Frameworks: Java 8 (Time API, Streams), Guava, Java Executor, MyBatis, Spring (MVC, Security, Data, Clouds, Boot), JPA\n" +
                "(Hibernate, EclipseLink), Guice, GWT(SmartGWT, ExtGWT/GXT), Vaadin, Jasperreports, Apache Commons, Eclipse SWT, JUnit,\n" +
                "Selenium (htmlelements).");
        resume1.addSection(SectionType.QUALIFICATIONS, new ListSection("Квалификация", qualificationList));

        List<Period> periods1 = new ArrayList<>();
        periods1.add(new Period(LocalDate.of(2013, 10, 1),
                LocalDate.now(), "Автор проекта.", "   Создание, организация и проведение Java онлайн проектов и стажировок."));
        Organization experience1 = new Organization("Java Online Projects", "http://javaops.ru/", periods1);

        List<Period> periods2 = new ArrayList<>();
        periods1.add(new Period(LocalDate.of(2014, 10, 1),
                LocalDate.of(2016, 1, 1), "Старший разработчик (backend)", "   Проектирование и разработка онлайн платформы управления проектами Wrike (Java 8 API, Maven, Spring, MyBatis,\n" +
                "   Guava, Vaadin, PostgreSQL, Redis). Двухфакторная аутентификация, авторизация по OAuth1, OAuth2, JWT SSO."));
        Organization experience2 = new Organization("Wrike", "https://www.wrike.com/", periods2);

        List<Organization> experienceList = new ArrayList<>();
        experienceList.add(experience1);
        experienceList.add(experience2);
        resume1.addSection(SectionType.EXPERIENCE, new OrganizationSection("Опыт работы", experienceList));

        List<Period> periods3 = new ArrayList<>();
        periods3.add(new Period(LocalDate.of(2013, 3, 1),
                LocalDate.of(2013, 5, 1), "Functional Programming Principles in Scala\" by Martin Odersky", null));
        Organization education1 = new Organization("Coursera", "https://www.coursera.org/course/progfun", periods3);

        List<Period> periods4 = new ArrayList<>();
        periods4.add(new Period(LocalDate.of(1993, 9, 1), LocalDate.of(1996, 7, 1),
                "Аспирантура (программист С, С++)", null));
        periods4.add(new Period(LocalDate.of(1987, 9, 1), LocalDate.of(1993, 7, 1),
                "Инженер (программист Fortran, C)", null));
        Organization education2 = new Organization("Санкт-Петербургский национальный исследовательский университет информационных технологий, механики и оптики",
                "http://www.ifmo.ru/", periods4);
        List<Organization> educationList = new ArrayList<>();
        educationList.add(education1);
        educationList.add(education2);
        resume1.addSection(SectionType.EDUCATION, new OrganizationSection("Образование", educationList));

        System.out.println(resume1.getFullName());
        System.out.println("----------------------------------------------");
        for (ContactType type : ContactType.values()) {
            System.out.println(type.getTitle() + ":  " + resume1.getContact(type));
        }
        System.out.println("----------------------------------------------");
        for (SectionType type : SectionType.values()) {
            System.out.println(resume1.getSection(type));
            System.out.println("----------------------------------------------");
        }
    }
}
