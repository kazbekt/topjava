package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.util.List;

@Controller
public class MealRestController {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private MealService service;

    public List<MealTo> getAll() {
        log.info("getAll mealsTo");
        return MealsUtil.getTos(service.getAll(), MealsUtil.DEFAULT_CALORIES_PER_DAY, SecurityUtil.authUserCaloriesPerDay());
    }

    public Meal get(int id, int authUserId) {
        log.info("get meal with id={}", id);
        return service.get(id, SecurityUtil.authUserId());
    }

    public Meal create(Meal meal, int authUserId) {
        log.info("create meal {}", meal);
        return service.create(meal, SecurityUtil.authUserId());
    }

    public void delete(int id, int authUserId) {
        log.info("delete meal with id={}", id);
        service.delete(id, SecurityUtil.authUserId());
    }

    public void update(Meal meal, int id, int authUserId) {
        log.info("update meal {} with id={}", meal, id);
        service.update(meal, id, SecurityUtil.authUserId());
    }
}