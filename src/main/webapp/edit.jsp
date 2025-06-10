<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="ru.javawebinar.topjava.model.Meal" %>
<%
  Meal meal = (Meal) request.getAttribute("meal");
  if (meal == null) {
    meal = new Meal(java.time.LocalDateTime.now(), "", 0);
  }
  String formattedDateTime = meal.getDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"));
%>
<html>
<head>
  <title><%= (meal.getUuid() == null ? "Добавить еду" : "Редактировать еду") %></title>
  <style>
    .form-group {
      margin-bottom: 15px;
    }
    label {
      display: inline-block;
      width: 120px;
    }
    input[type="text"], input[type="datetime-local"], input[type="number"] {
      width: 250px;
      padding: 6px;
    }
    .btn {
      padding: 8px 15px;
      background-color: #4CAF50;
      color: white;
      border: none;
      border-radius: 4px;
      cursor: pointer;
    }
    .btn:hover {
      background-color: #45a049;
    }
  </style>
</head>
<body>
<h2><%= (meal.getUuid() == null ? "Добавить еду" : "Редактировать еду") %></h2>

<form method="post" action="meals">
  <input type="hidden" name="uuid" value="<%= meal.getUuid() != null ? meal.getUuid() : "" %>">

  <div class="form-group">
    <label for="dateTime">Дата и время:</label>
    <input type="datetime-local" id="dateTime" name="dateTime"
           value="<%= formattedDateTime %>" required>
  </div>

  <div class="form-group">
    <label for="description">Описание:</label>
    <input type="text" id="description" name="description"
           value="<%= meal.getDescription() %>" required>
  </div>

  <div class="form-group">
    <label for="calories">Калории:</label>
    <input type="number" id="calories" name="calories"
           value="<%= meal.getCalories() %>" min="0" required>
  </div>

  <button type="submit" class="btn">Сохранить</button>
  <a href="meals" style="margin-left: 10px;">Отмена</a>
</form>
</body>
</html>