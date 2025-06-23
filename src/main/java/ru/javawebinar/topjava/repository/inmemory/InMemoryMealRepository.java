package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private final Map<Integer, Meal> mealsMap = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.meals.forEach(m -> save(m, MealsUtil.USER_ID));
    }

    @Override
    public Meal save(Meal meal, int userId) {
            meal.setUserId(userId);

        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            mealsMap.put(meal.getId(), meal);
            return meal;
        }
        Meal oldMeal = mealsMap.get(meal.getId());

        if (oldMeal == null || isNotUserMeal(oldMeal.getUserId(), userId)) {
            return null;
        } else {
            return mealsMap.computeIfPresent(meal.getId(), (id, oMeal) -> meal);
        }
    }

    @Override
    public boolean delete(int mealId, int userId) {
        Meal meal = mealsMap.get(mealId);
        if (meal == null || isNotUserMeal(meal.getUserId(), userId)) {
            return false;
        }
        return mealsMap.remove(mealId) != null;
    }

    @Override
    public Meal get(int mealId, int userId) {
        Meal meal = mealsMap.get(mealId);
        if (meal == null || isNotUserMeal(meal.getUserId(), userId)) {
            return null;
        }
        return meal;
    }

    @Override
    public List<Meal> getAll(int userId) {
        return getBetween(userId, LocalDate.MIN, LocalDate.MAX);
    }

    public List<Meal> getBetween(int userId, LocalDate startDate, LocalDate endDate) {
        return mealsMap.values().stream()
                .filter(meal -> meal.getUserId() == userId)
                .filter(meal -> DateTimeUtil.isBetweenHalfOpen(meal.getDateTime(), startDate, endDate))
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .collect(Collectors.toList());
    }

    private boolean isNotUserMeal(int mealUserId, int userId) {
        return mealUserId != userId;
    }
}