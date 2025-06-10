package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.storage.ListStorage;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private ListStorage storage;
    private static final Logger log = getLogger(MealServlet.class);

    @Override
    public void init() {
        storage = new ListStorage();
        MealsUtil.meals.forEach(storage::save);
        getServletContext().setAttribute("mealStorage", storage);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");

        if (action == null) {
            showMealsList(request, response);
            return;
        }

        switch (action) {
            case "delete":
                deleteMeal(request, response);
                break;
            case "edit":
            case "add":
                showEditForm(request, response);
                break;
            default:
                throw new IllegalArgumentException("Unknown action: " + action);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");

        String uuidParam = request.getParameter("uuid");
        String description = request.getParameter("description");
        String dateTimeStr = request.getParameter("dateTime");
        int calories = Integer.parseInt(request.getParameter("calories"));
        LocalDateTime dateTime = LocalDateTime.parse(dateTimeStr);

        Meal meal;

        if (uuidParam == null || uuidParam.isEmpty()) {
            meal = new Meal(dateTime, description, calories);
            storage.save(meal);
        } else {
            meal = storage.get(uuidParam);
            meal.setDateTime(dateTime);
            meal.setDescription(description);
            meal.setCalories(calories);
            storage.update(meal);
        }

        response.sendRedirect("meals");
    }

    private void showMealsList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("Show meals list");
        List<MealTo> mealsTo = MealsUtil.createToByStreams(storage.getAllMeal(), MealsUtil.CALORIES_PER_DAY);
        request.setAttribute("mealsTo", mealsTo);
        request.getRequestDispatcher("/meals.jsp").forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String uuid = request.getParameter("uuid");
        Meal meal;

        if (uuid != null && !uuid.isEmpty()) {
            meal = storage.get(uuid);
        } else {
            meal = new Meal(LocalDateTime.now(), "", 0);
        }

        request.setAttribute("meal", meal);
        request.getRequestDispatcher("/edit.jsp").forward(request, response);
    }

    private void deleteMeal(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String uuid = request.getParameter("uuid");
        log.debug("Delete meal with uuid={}", uuid);
        storage.delete(uuid);
        response.sendRedirect("meals");
    }

}
