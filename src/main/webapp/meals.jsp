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
    </tr>
    <c:forEach items="${mealsTo}" var="mealTo">
        <tr class="${mealTo.excess ? 'excess-row' : 'normal-row'}">
            <td>${f:formatLocalDateTime(mealTo.dateTime)}</td>
            <td>${mealTo.description}</td>
            <td>${mealTo.calories}</td>
        </tr>
    </c:forEach>
</table>
</body>
</html>