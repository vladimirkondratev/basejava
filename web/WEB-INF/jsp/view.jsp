<%@ page import="ru.javawebinar.basejava.model.TextSection" %>
<%@ page import="ru.javawebinar.basejava.model.ListSection" %>
<%@ page import="ru.javawebinar.basejava.model.OrganizationSection" %>
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
    <h1>${resume.fullName}&nbsp;<a href="resume?uuid=${resume.uuid}&action=edit"><img src="img/pencil.png"></a></h1>
    <p>
        <c:forEach var="contactEntry" items="${resume.contacts}">
            <jsp:useBean id="contactEntry"
                         type="java.util.Map.Entry<ru.javawebinar.basejava.model.ContactType, java.lang.String>"/>
            <%=contactEntry.getKey().toHtml(contactEntry.getValue())%><br/>
        </c:forEach>
    </p>
    <c:forEach var="sectionEntry" items="${resume.sections}">
        <jsp:useBean id="sectionEntry"
                     type="java.util.Map.Entry<ru.javawebinar.basejava.model.SectionType, ru.javawebinar.basejava.model.AbstractSection>"/>
        <c:set var="type" value="${sectionEntry.key}"/>
        <c:set var="section" value="${sectionEntry.value}"/>
        <jsp:useBean id="section" type="ru.javawebinar.basejava.model.AbstractSection"/>
        <c:choose>
            <c:when test="${type=='OBJECTIVE' || type=='PERSONAL'}">
                <h2>${type.title}</h2>
                <%=((TextSection) section).getText()%>
            </c:when>
            <c:when test="${type=='ACHIEVEMENT' || type=='QUALIFICATIONS'}">
                <h2>${type.title}</h2>
                <ul>
                    <c:forEach var="item" items="<%=((ListSection) section).getItems()%>">
                        <li>${item}</li>
                    </c:forEach>
                </ul>
            </c:when>
            <c:when test="${type=='EXPERIENCE' || type=='EDUCATION'}">
                <h2>${type.title}</h2>
                <table cellpadding="2">
                    <c:forEach var="organization" items="<%=((OrganizationSection) section).getItems()%>">
                        <c:forEach var="position" items="${organization.positions}">
                            <jsp:useBean id="position" type="ru.javawebinar.basejava.model.Organization.Position"/>
                            <tr>
                                <td colspan="2">
                                    <h3><a href="${organization.homepage.url}">${organization.homepage.name}</a></h3>
                                </td>
                            </tr>
                            <tr>
                                <td width="20%" style="vertical-align: top"> <%=DateUtil.format(position.getStartDate())%> - <%=DateUtil.format(position.getEndDate())%>
                                </td>
                                <td>
                                    <b>${position.title}</b><br/>
                                        ${position.description}
                                </td>
                            </tr>
                        </c:forEach>
                    </c:forEach>
                </table>
            </c:when>
        </c:choose>
    </c:forEach>
</section>
</body>
</html>