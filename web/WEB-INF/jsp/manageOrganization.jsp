<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <link rel="stylesheet" href="css/style.css">
    <title>Добавить организацию</title>
</head>
<body>
<section>
    <form method="post" action="manageOrganization?uuid=${uuid}&type=${type}" enctype="application/x-www-form-urlencoded">
        <dl>
            <dt>Организация</dt>
            <dd><input type="text" name="organization" size=100 value="${organization.homepage.name}"></dd>
        </dl>
        <dl>
            <dt>Сайт</dt>
            <dd><input type="text" name="homepage" size=50 value="${organization.homepage.url}"></dd>
        </dl>
        <c:if test="${uuid != ''}">
            <button type="submit">Сохранить</button>
        </c:if>
        <button onclick="window.history.back()">Отменить</button>
    </form>
</section>
</body>
</html>
