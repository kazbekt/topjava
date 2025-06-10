<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib  prefix="f" uri="topjava/functions" %>

<html lang="ru">

<head>
    <title>Список еды</title>
    <style>
        table {
            border-collapse: collapse;
        }

        th, td {
            border: 1px solid black;
            padding: 5px;
        }

        th {
            background-color: #f2f2f2;
        }

        .excess-row {
            background-color: #FFDDDD;
        }

        .normal-row {
            background-color: #D4EDDA;
        }

        .add-resume-btn {
            display: inline-block;
            margin: 15px 0;
            padding: 8px 15px;
            background-color: #4CAF50;
            color: white;
            text-decoration: none;
            border-radius: 4px;
            font-weight: bold;
        }
        .add-resume-btn:hover {
            background-color: #45a049;
        }
    </style>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>Meals</h2>
<table>
    <tr>
        <th>Date/Time</th>
        <th>Description</th>
        <th>Calories</th>
        <th>Edit</th>
        <th>Delete</th>
    </tr>
    <c:forEach items="${mealsTo}" var="mealTo">
        <tr class="${mealTo.excess ? 'excess-row' : 'normal-row'}">
            <td>${f:formatLocalDateTime(mealTo.dateTime)}</td>
            <td>${mealTo.description}</td>
            <td>${mealTo.calories}</td>
            <td><a href="meals?action=edit&uuid=${mealTo.uuid}">Обновить</a></td>
            <td><a href="meals?action=delete&uuid=${mealTo.uuid}">Удалить</a></td>

        </tr>
    </c:forEach>
</table>
<div style="text-align: left">
    <a href="meals?action=add" class="add-resume-btn">Добавить еду</a>
</div>
</body>
</html>