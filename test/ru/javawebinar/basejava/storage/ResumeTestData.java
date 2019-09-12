package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.*;
import ru.javawebinar.basejava.util.DateUtil;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

public class ResumeTestData {

    private static final String UUID_1 = "uuid1";
    private static final String UUID_2 = "uuid2";
    private static final String UUID_3 = "uuid3";
    private static final String UUID_4 = "uuid4";

    private static Resume resume1 = new Resume(UUID_1, "fullName1");
    private static Resume resume2 = new Resume(UUID_2, "fullName2");
    private static Resume resume3 = new Resume(UUID_3, "fullName3");
    private static Resume resume4 = new Resume(UUID_4, "fullName4");

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
        periods1.add(new Period(DateUtil.of(2013, Month.OCTOBER),
                                LocalDate.now(),
                           "Автор проекта.",
                      "Создание, организация и проведение Java онлайн проектов и стажировок."));
        Organization experience1 = new Organization("Java Online Projects",
                                                      "http://javaops.ru/",
                                                           periods1);

        List<Period> periods2 = new ArrayList<>();
        periods1.add(new Period(DateUtil.of(2014, Month.OCTOBER),
                                DateUtil.of(2016, Month.JANUARY),
                            "Старший разработчик (backend)",
                       "Проектирование и разработка онлайн платформы управления проектами Wrike (Java 8 API, Maven, Spring, MyBatis,\n" +
                                 "Guava, Vaadin, PostgreSQL, Redis). Двухфакторная аутентификация, авторизация по OAuth1, OAuth2, JWT SSO."));
        Organization experience2 = new Organization("Wrike",
                                                      "https://www.wrike.com/",
                                                          periods2);

        List<Organization> experienceList = new ArrayList<>();
        experienceList.add(experience1);
        experienceList.add(experience2);
        resume1.addSection(SectionType.EXPERIENCE, new OrganizationSection("Опыт работы", experienceList));

        List<Period> periods3 = new ArrayList<>();
        periods3.add(new Period(DateUtil.of(2013, Month.MARCH),
                                DateUtil.of(2013, Month.MAY),
                           "Functional Programming Principles in Scala\" by Martin Odersky",
                       null));
        Organization education1 = new Organization("Coursera",
                                                     "https://www.coursera.org/course/progfun",
                                                         periods3);

        List<Period> periods4 = new ArrayList<>();
        periods4.add(new Period(DateUtil.of(1993, Month.SEPTEMBER),
                                DateUtil.of(1996, Month.JULY),
                                "Аспирантура (программист С, С++)",
                                null));
        periods4.add(new Period(DateUtil.of(1987, Month.SEPTEMBER),
                                DateUtil.of(1993, Month.JULY),
                                "Инженер (программист Fortran, C)",
                                null));
        Organization education2 = new Organization("Санкт-Петербургский национальный исследовательский университет информационных технологий, механики и оптики",
                                                    "http://www.ifmo.ru/",
                                                    periods4);
        List<Organization> educationList = new ArrayList<>();
        educationList.add(education1);
        educationList.add(education2);
        resume1.addSection(SectionType.EDUCATION, new OrganizationSection("Образование", educationList));
    }

    public static Resume getResume(String uuid, String fullName) {
        switch (uuid){
            case UUID_1: return resume1;
            case UUID_2: return resume2;
            case UUID_3: return resume3;
            case UUID_4: return resume4;
            default: return null;
        }
    }

}
