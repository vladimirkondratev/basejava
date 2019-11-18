<%@ page import="ru.javawebinar.basejava.model.ContactType" %>
<%@ page import="ru.javawebinar.basejava.model.ListSection" %>
<%@ page import="ru.javawebinar.basejava.model.OrganizationSection" %>
<%@ page import="ru.javawebinar.basejava.model.SectionType" %>
<%@ page import="ru.javawebinar.basejava.util.DateUtil" %>
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
                        <b>
                            <dt><h3>${type.title}</h3></dt>
                        </b>
                        <dd><input type="text" name="${type.name()}" size=100 value="${section.text}"></dd>
                    </dl>
                </c:when>
                <c:when test="${type=='ACHIEVEMENT' || type=='QUALIFICATIONS'}">
                    <dl>
                        <b>
                            <dt><h3>${type.title}</h3></dt>
                        </b>
                        <dd><textarea rows="5" cols="100" name="${type.name()}"><c:if
                                test="${resume.getSection(type) != null}"><jsp:useBean id="section"
                                         type="ru.javawebinar.basejava.model.AbstractSection"/><%=String.join("\n", ((ListSection) section).getItems())%></c:if></textarea></dd>
                    </dl>
                </c:when>
                <c:when test="${type=='EXPERIENCE' || type=='EDUCATION'}">
                    <br>
                    <b><h3>${type.title}</h3></b>
                    <br>
                    <c:if test="${resume.getSection(type) != null}">
                        <c:set var="section2" value="${resume.getSection(type)}"/>
                        <jsp:useBean id="section2" type="ru.javawebinar.basejava.model.AbstractSection"/>
                        <c:forEach var="organization" items="<%=((OrganizationSection) section2).getItems()%>"
                                   varStatus="counterOrg">
                            <div class="div_organization">
                                <dl>
                                    <dt><b>Организация</b></dt>
                                    <dd><input type="text" name="${type}" size=100
                                               value="${organization.homepage.name}"></dd>
                                </dl>
                                <dl>
                                    <dt>Сайт</dt>
                                    <dd><input type="text" name="${type}homepage" size=30
                                               value="${organization.homepage.url}"></dd>
                                </dl>
                                <c:forEach var="position" items="${organization.positions}" varStatus="counterPos">
                                    <jsp:useBean id="position"
                                                 type="ru.javawebinar.basejava.model.Organization.Position"/>
                                    <div>
                                        <dl>
                                            <dt>Позиция/Должность</dt>
                                            <dd><input type="text" name="${type}${counterOrg.index}title" size=30
                                                       value="${position.title}"></dd>
                                        </dl>
                                        <dl>
                                            <dt>Дата начала</dt>
                                            <dd><input type="text" name="${type}${counterOrg.index}startDate" size=30
                                                       value="<%=DateUtil.format(position.getStartDate())%>"
                                                       placeholder="MM/yyyy">
                                            </dd>
                                        </dl>
                                        <dl>
                                            <dt>Дата окончания</dt>
                                            <dd><input type="text" name="${type}${counterOrg.index}endDate" size=30
                                                       value="<%=DateUtil.format(position.getEndDate())%>"
                                                       placeholder="MM/yyyy">
                                            </dd>
                                        </dl>
                                        <dl>
                                            <dt>Описание</dt>
                                            <dd><input type="text" name="${type}${counterOrg.index}description" size=100
                                                       value="${position.description}"></dd>
                                        </dl>
                                    </div>
                                    <hr>
                                </c:forEach>
                            </div>
                        </c:forEach>
                    </c:if>
                </c:when>
            </c:choose>
        </c:forEach>
        <button type="submit">Сохранить</button>
        <button onclick="window.history.back()">Отменить</button>
    </form>
</section>
</body>
</html>


