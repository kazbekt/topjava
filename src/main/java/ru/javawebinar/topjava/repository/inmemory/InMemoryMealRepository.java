package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;

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
        MealsUtil.meals.forEach(m -> save(m, MealsUtil.userId));
    }

    @Override
    public Meal save(Meal meal, int userId) {
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
    public List<Meal> getAll() {
        return mealsMap.values().stream()
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .collect(Collectors.toList());
    }

    private boolean isNotUserMeal(int mealUserId, int userId) {
        return mealUserId != userId;
    }
}