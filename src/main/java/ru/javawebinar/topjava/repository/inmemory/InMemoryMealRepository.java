package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private final Map<Integer, Meal> mealsMap = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.meals.forEach(m -> save(m, MealsUtil.user.getId()));
    }

    @Override
    public Meal save(Meal meal, int authUserId) {
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            mealsMap.put(meal.getId(), meal);
            return meal;
        }
        while (true) {
            Meal oldMeal = mealsMap.get(meal.getId());

            if (oldMeal == null) {
                return null;
            }

            if (isNotUserMeal(oldMeal.getUserId(), authUserId)) {
                return null;
            }

            if (mealsMap.replace(meal.getId(), oldMeal, meal)) {
                return meal;
            }
        }
    }

    @Override
    public boolean delete(int mealId, int authUserId) {
        if (isNotUserMeal(mealsMap.get(mealId).getUserId(), authUserId)) {
            return false;
        }
        return mealsMap.remove(mealId) != null;
    }

    @Override
    public Meal get(int mealId, int authUserId) {
        if (isNotUserMeal(mealsMap.get(mealId).getUserId(), authUserId)) {
            return null;
        }
        return mealsMap.get(mealId);
    }

    @Override
    public Collection<Meal> getAll() {
        return mealsMap.values().stream()
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .collect(Collectors.toList());
    }

    private boolean isNotUserMeal(int mealUserId, int authUserId) {
        return mealUserId != authUserId;
    }
}