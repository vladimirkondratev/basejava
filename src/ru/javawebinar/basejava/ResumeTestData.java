package ru.javawebinar.basejava;

import ru.javawebinar.basejava.model.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ResumeTestData {
    public static void main(String[] args) {
        Resume resume = new Resume("Name Surname");

        resume.putContact(ContactType.PHONE, "+7(900) 111-1111");
        resume.putContact(ContactType.EMAIL, "account@yandex.ru");
        resume.putContact(ContactType.SKYPE, "skype.skype");

        resume.putSection(SectionType.OBJECTIVE, new TextSection("Позиция", "Ведущий стажировок и корпоративного обучения по Java Web и Enterprise технологиям."));
        resume.putSection(SectionType.PERSONAL, new TextSection("Личные качества", "Аналитический склад ума, сильная логика, креативность, инициативность. Пурист кода и архитектуры."));

        List<String> achievementList = new ArrayList<>();
        achievementList.add("С 2013 года: разработка проектов \"Разработка Web приложения\",\"Java Enterprise\", \"Многомодульный maven.\n" +
                "Многопоточность. XML (JAXB/StAX). Веб сервисы (JAX-RS/SOAP). Удаленное взаимодействие (JMS/AKKA)\".\n" +
                "Организация онлайн стажировок и ведение проектов. Более 1000 выпускников.");
        achievementList.add("Реализация двухфакторной аутентификации для онлайн платформы управления проектами Wrike. Интеграция с Twilio,\n" +
                "DuoSecurity, Google Authenticator, Jira, Zendesk.");
        achievementList.add("Налаживание процесса разработки и непрерывной интеграции ERP системы River BPM. Интеграция с 1С, Bonita BPM,\n" +
                "CMIS, LDAP. Разработка приложения управления окружением на стеке: Scala/Play/Anorm/JQuery. Разработка SSO\n" +
                "аутентификации и авторизации различных ERP модулей, интеграция CIFS/SMB java сервера.");
        resume.putSection(SectionType.ACHIEVEMENT, new ListSection("Достижения", achievementList));

        List<String> qualificationList = new ArrayList<>();
        qualificationList.add("JEE AS: GlassFish (v2.1, v3), OC4J, JBoss, Tomcat, Jetty, WebLogic, WSO2");
        qualificationList.add("Version control: Subversion, Git, Mercury, ClearCase, Perforce");
        qualificationList.add("Java Frameworks: Java 8 (Time API, Streams), Guava, Java Executor, MyBatis, Spring (MVC, Security, Data, Clouds, Boot), JPA\n" +
                "(Hibernate, EclipseLink), Guice, GWT(SmartGWT, ExtGWT/GXT), Vaadin, Jasperreports, Apache Commons, Eclipse SWT, JUnit,\n" +
                "Selenium (htmlelements).");
        resume.putSection(SectionType.QUALIFICATIONS, new ListSection("Квалификация", qualificationList));

        DateItem experience1 = new DateItem(LocalDate.of(2013, 10, 1),
                null, "Java Online Projects", "Автор проекта.\n" +
                "   Создание, организация и проведение Java онлайн проектов и стажировок.");
        DateItem experience2 = new DateItem(LocalDate.of(2014, 10, 1),
                LocalDate.of(2016, 1, 1), "Wrike", "Старший разработчик (backend)\n" +
                "   Проектирование и разработка онлайн платформы управления проектами Wrike (Java 8 API, Maven, Spring, MyBatis,\n" +
                "   Guava, Vaadin, PostgreSQL, Redis). Двухфакторная аутентификация, авторизация по OAuth1, OAuth2, JWT SSO.");
        DateItem experience3 = new DateItem(LocalDate.of(2012, 4, 1),
                LocalDate.of(2016, 1, 1), "RIT Center",
                "Java архитектор\n" +
                        "   Организация процесса разработки системы ERP для разных окружений: релизная политика, версионирование,\n" +
                        "   ведение CI (Jenkins), миграция базы (кастомизация Flyway), конфигурирование системы (pgBoucer, Nginx),\n" +
                        "   AAA via SSO. Архитектура БД и серверной части системы. Разработка интергационных сервисов: CMIS,\n" +
                        "   BPMN2, 1C (WebServices), сервисов общего назначения (почта, экспорт в pdf, doc, html). Интеграция Alfresco\n" +
                        "   JLAN для online редактирование из браузера документов MS Office. Maven + plugin development, Ant, Apache\n" +
                        "   Commons, Spring security, Spring MVC, Tomcat,WSO2, xcmis, OpenCmis, Bonita, Python scripting, Unix shell\n" +
                        "   remote scripting via ssh tunnels, PL/Python");

        List<DateItem> experienceList = new ArrayList<>();
        experienceList.add(experience1);
        experienceList.add(experience2);
        experienceList.add(experience3);
        resume.putSection(SectionType.EXPERIENCE, new DateSection("Опыт работы", experienceList));

        DateItem education1 = new DateItem(LocalDate.of(2013, 3, 1),
                LocalDate.of(2013, 5, 1), "Coursera", "Functional Programming Principles in Scala\" by Martin Odersky");
        DateItem education2 = new DateItem(LocalDate.of(2011, 3, 1),
                LocalDate.of(2014, 4, 1), "Luxoft", "Курс \"Объектно-ориентированный анализ ИС. Концептуальное моделирование на UML.");
        DateItem education3 = new DateItem(LocalDate.of(2005, 1, 1),
                LocalDate.of(2005, 4, 1), "Siemens AG", "3 месяца обучения мобильным IN сетям (Берлин)");
        List<DateItem> educationList = new ArrayList<>();
        educationList.add(education1);
        educationList.add(education2);
        educationList.add(education3);
        resume.putSection(SectionType.EDUCATION, new DateSection("Образование", educationList));

        System.out.println(resume.getFullName());
        System.out.println("----------------------------------------------");
        for (ContactType type : ContactType.values()) {
            System.out.println(type.getTitle() + ":  " + resume.getContact(type));
        }
        System.out.println("----------------------------------------------");
        for (SectionType type : SectionType.values()) {
            System.out.println(resume.getSection(type));
            System.out.println("----------------------------------------------");
        }
    }
}
