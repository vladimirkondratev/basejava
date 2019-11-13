<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link rel="stylesheet" href="css/style.css">
    <title>Добавить позицию/должность</title>
</head>
<body>
<section>
    <form method="post" action="managePosition?uuid=${uuid}&type=${type}&organization=${organization}" enctype="application/x-www-form-urlencoded">
        <dl>
            <dt>Организация</dt>
            <dd><input type="text" name="homepage" size=30 readonly value="${requestScope.get("organization")}"></dd>
        </dl>
        <dl>
            <dt>Позиция/Должность</dt>
            <dd><input type="text" name="positionName" size=30></dd>
        </dl>
        <dl>
            <dt>Дата начала</dt>
            <dd><input type="text" name="positionStartDate" size=30 placeholder="MM/yyyy"></dd>
        </dl>
        <dl>
            <dt>Дата окончания</dt>
            <dd><input type="text" name="positionEndDate" size=30" placeholder="MM/yyyy"></dd>
        </dl>
        <dl>
            <dt>Описание</dt>
            <dd><input type="text" name="positionDescription" size=100></dd>
        </dl>
        <button type="submit">Сохранить</button>
        <button onclick="window.history.back()">Отменить</button>
    </form>
</section>
</body>
</html>