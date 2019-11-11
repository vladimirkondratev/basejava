<%@ page import="ru.javawebinar.basejava.model.ContactType" %>
<%@ page import="ru.javawebinar.basejava.model.ListSection" %>
<%@ page import="ru.javawebinar.basejava.model.SectionType" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="css/style.css">
    <jsp:useBean id="resume" type="ru.javawebinar.basejava.model.Resume" scope="request"/>
    <title>Резюме ${resume.fullName}</title>
</head>
<body>
<jsp:include page="fragments/header.jsp"/>
<section>
    <form method="post" action="resume" enctype="application/x-www-form-urlencoded">
        <input type="hidden" name="uuid" value="${resume.uuid}">
        <dl>
            <dt>Имя:</dt>
            <dd><input type="text" name="fullName" size=50 value="${resume.fullName}"></dd>
        </dl>
        <h3>Контакты:</h3>
        <c:forEach var="type" items="<%=ContactType.values()%>">
            <dl>
                <dt>${type.title}</dt>
                <dd><input type="text" name="${type.name()}" size=30 value="${resume.getContact(type)}"></dd>
            </dl>
        </c:forEach>

        <c:forEach var="type" items="<%=SectionType.values()%>">
            <c:set var="section" value="${resume.getSection(type)}"/>
            <c:choose>
                <c:when test="${type=='OBJECTIVE' || type=='PERSONAL'}">
                    <dl>
                        <dt>${type.title}</dt>
                        <dd><input type="text" name="${type.name()}" size=100 value="${section.text}"></dd>
                    </dl>
                </c:when>
                <c:when test="${type=='ACHIEVEMENT' || type=='QUALIFICATIONS'}">
                    <dl>
                        <dt>${type.title}</dt>
                        <dd><textarea rows="5" cols="100" name="${type.name()}"><c:if
                                test="${resume.getSection(type) != null}"><jsp:useBean
                                id="section" type="ru.javawebinar.basejava.model.AbstractSection"/><%=String.join("\n", ((ListSection) section).getItems())%></c:if></textarea></dd>
                    </dl>
                </c:when>
            </c:choose>
        </c:forEach>
        <hr>
        <button type="submit">Сохранить</button>
        <button onclick="window.history.back()">Отменить</button>
    </form>
</section>
</body>
</html>

