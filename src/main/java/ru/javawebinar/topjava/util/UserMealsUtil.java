package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collector;

import static ru.javawebinar.topjava.util.TimeUtil.isBetweenHalfOpen;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> meals = Arrays.asList(
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410));

        List<UserMealWithExcess> mealsTo1 = filteredByCycle(meals, LocalTime.of(0, 0), LocalTime.of(21, 1), 2000);
        mealsTo1.forEach(System.out::println);

        System.out.println("-------------------------");

        List<UserMealWithExcess> mealsTo2 = filteredByStream(meals, LocalTime.of(0, 0), LocalTime.of(21, 1), 2000);
        mealsTo2.forEach(System.out::println);

    }

    public static List<UserMealWithExcess> filteredByCycle(
            List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> dayCal = new HashMap<>();
        List<UserMealWithExcess> mealsWithExcess = new ArrayList<>();

        for (UserMeal meal : meals) {
            dayCal.merge(meal.getDateTime().toLocalDate(), meal.getCalories(), Integer::sum);

            if (isBetweenHalfOpen(meal.getDateTime().toLocalTime(), startTime, endTime)) {
                mealsWithExcess.add(new UserMealWithExcess(
                        meal.getDateTime(),
                        meal.getDescription(),
                        meal.getCalories(),
                        dayCal,
                        caloriesPerDay));
            }
        }
        return mealsWithExcess;
    }

    public static List<UserMealWithExcess> filteredByStream(
            List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {

        class MealAccumulator {
            final List<UserMealWithExcess> mealsWithExcess = new ArrayList<>();
            final Map<LocalDate, Integer> dayCal = new HashMap<>();
        }

        MealAccumulator acc = meals.stream().collect(
                Collector.of(
                        MealAccumulator::new,
                        (ma, meal) -> {
                            LocalDate date = meal.getDateTime().toLocalDate();
                            ma.dayCal.merge(date, meal.getCalories(), Integer::sum);

                            if (isBetweenHalfOpen(meal.getDateTime().toLocalTime(), startTime, endTime)) {
                                ma.mealsWithExcess.add(new UserMealWithExcess(
                                        meal.getDateTime(),
                                        meal.getDescription(),
                                        meal.getCalories(),
                                        ma.dayCal,
                                        caloriesPerDay
                                ));
                            }
                        },
                        (ma1, ma2) -> {
                            throw new UnsupportedOperationException();
                        }
                )
        );
        return acc.mealsWithExcess;
    }
}