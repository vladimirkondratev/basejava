DELETE
FROM contact;
DELETE
FROM section;
DELETE
FROM resume;

INSERT INTO resume (uuid, full_name)
VALUES ('uuid1', 'fullName1'),
       ('uuid2', 'fullName2'),
       ('uuid3', 'fullName3'),
       ('uuid4', 'fullName4');

INSERT INTO contact(id, resume_uuid, type, value)
VALUES ('1', 'uuid1', 'PHONE', '12345'),
       ('2', 'uuid1', 'SKYPE', 'skype');

INSERT INTO section(id, resume_uuid, type, value)
VALUES ('1', 'uuid1', 'OBJECTIVE',
        'Ведущий стажировок и корпоративного обучения по Java Web и Enterprise технологиям.'),
       ('2', 'uuid1', 'PERSONAL',
        'Аналитический склад ума, сильная логика, креативность, инициативность. Пурист кода и архитектуры.'),
       ('3', 'uuid1', 'ACHIEVEMENT',
        'С 2013 года: разработка проектов "Разработка Web приложения","Java Enterprise", "Многомодульный maven. Многопоточность. XML (JAXB/StAX). Веб сервисы (JAX-RS/SOAP). Удаленное взаимодействие (JMS/AKKA)". Организация онлайн стажировок и ведение проектов. Более 1000 выпускников.
Реализация двухфакторной аутентификации для онлайн платформы управления проектами Wrike. Интеграция с Twilio, DuoSecurity, Google Authenticator, Jira, Zendesk.
Налаживание процесса разработки и непрерывной интеграции ERP системы River BPM. Интеграция с 1С, Bonita BPM, CMIS, LDAP. Разработка приложения управления окружением на стеке: Scala/Play/Anorm/JQuery. Разработка SSO аутентификации и авторизации различных ERP модулей, интеграция CIFS/SMB java сервера.'),
       ('4', 'uuid1', 'QUALIFICATIONS',
        'JEE AS: GlassFish (v2.1, v3), OC4J, JBoss, Tomcat, Jetty, WebLogic, WSO2
Version control: Subversion, Git, Mercury, ClearCase, Perforce
Java Frameworks: Java 8 (Time API, Streams), Guava, Java Executor, MyBatis, Spring (MVC, Security, Data, Clouds, Boot), JPA (Hibernate, EclipseLink), Guice, GWT(SmartGWT, ExtGWT/GXT), Vaadin, Jasperreports, Apache Commons, Eclipse SWT, JUnit, Selenium (htmlelements).');