package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.web.meal.MealRestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;

public class MealServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(MealServlet.class);

    private MealRestController mealRestController;
    private ConfigurableApplicationContext appCtx;

    @Override
    public void init() {
        appCtx = new ClassPathXmlApplicationContext("spring/spring-app.xml");
        mealRestController = appCtx.getBean(MealRestController.class);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        String id = request.getParameter("id");

        Meal meal = new Meal(id.isEmpty() ? null : Integer.valueOf(id),
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories")),
                SecurityUtil.authUserId());

        if (meal.isNew()) {
            log.info("Create {}", meal);
            mealRestController.create(meal);
        } else {
            log.info("Update {}", meal);
            mealRestController.update(meal, SecurityUtil.authUserId());
        }
        response.sendRedirect("meals");

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        Integer sessionUserId = (Integer) request.getSession().getAttribute("userId");
        SecurityUtil.setAuthUserId(sessionUserId != null ? sessionUserId : 1);

        switch (action == null ? "all" : action) {
            case "delete":
                int id = getId(request);
                log.info("Delete id={}", id);
                mealRestController.delete(id);
                response.sendRedirect("meals");
                break;
            case "create":
                final Meal newMeal = new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000, null);
                request.setAttribute("meal", newMeal);
                request.getRequestDispatcher("/mealForm.jsp").forward(request, response);
                break;
            case "update":
                int updateId = getId(request);
                Meal mealToUpdate = mealRestController.get(updateId);
                request.setAttribute("meal", mealToUpdate);
                request.getRequestDispatcher("/mealForm.jsp").forward(request, response);
                break;
            case "filter":
                String startDateParam = request.getParameter("startDate");
                String endDateParam = request.getParameter("endDate");
                String startTimeParam = request.getParameter("startTime");
                String endTimeParam = request.getParameter("endTime");

                LocalDate startDate = (startDateParam == null || startDateParam.isEmpty()) ? null : LocalDate.parse(startDateParam);
                LocalDate endDate = (endDateParam == null || endDateParam.isEmpty()) ? null : LocalDate.parse(endDateParam);
                LocalTime startTime = (startTimeParam == null || startTimeParam.isEmpty()) ? null : LocalTime.parse(startTimeParam);
                LocalTime endTime = (endTimeParam == null || endTimeParam.isEmpty()) ? null : LocalTime.parse(endTimeParam);

                List<MealTo> filteredMeals = mealRestController.getFiltered(
                        startDate,
                        endDate,
                        startTime,
                        endTime);

                request.setAttribute("meals", filteredMeals);
                request.getRequestDispatcher("/meals.jsp").forward(request, response);
                break;


            case "all":
            default:
                log.info("getAll");
                request.setAttribute("meals", mealRestController.getAll());
                request.getRequestDispatcher("/meals.jsp").forward(request, response);
                break;
        }
    }

    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.parseInt(paramId);
    }

    @Override
    public void destroy() {
        super.destroy();
        if (appCtx != null) {
            appCtx.close();
        }
    }
}
