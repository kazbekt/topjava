package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

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

        List<UserMealWithExcess> mealsTo1 = filteredByCycles(meals, LocalTime.of(0, 0), LocalTime.of(21, 1), 2000);
        mealsTo1.forEach(System.out::println);

        System.out.println("-------------------------");

        List<UserMealWithExcess> mealsTo2 = filteredByStreams(meals, LocalTime.of(0, 0), LocalTime.of(21, 1), 2000);
        mealsTo2.forEach(System.out::println);

        System.out.println("-------------------------");

        List<UserMealWithExcess> mealsTo3 = filteredByCycle(meals, LocalTime.of(0, 0), LocalTime.of(21, 1), 2000);
        mealsTo3.forEach(System.out::println);

        System.out.println("-------------------------");

        List<UserMealWithExcess> mealsTo4 = filteredByStream(meals, LocalTime.of(0, 0), LocalTime.of(21, 1), 2000);
        mealsTo4.forEach(System.out::println);

        //System.out.println(filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
    }

    public static List<UserMealWithExcess> filteredByCycles(
            List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {

        Map<LocalDate, Integer> dayCal = new HashMap<>();
        for (UserMeal meal : meals) {
            dayCal.put(
                    meal.getDateTime().toLocalDate(),
                    dayCal.getOrDefault(meal.getDateTime().toLocalDate(),
                            0) + meal.getCalories());
        }

        List<UserMealWithExcess> mealsWithExcess = new ArrayList<>();
        for (UserMeal meal : meals) {
            boolean excess = dayCal.get(meal.getDateTime().toLocalDate()) > caloriesPerDay;

            if (isBetweenHalfOpen(meal.getDateTime().toLocalTime(), startTime, endTime)) {
                mealsWithExcess.add(new UserMealWithExcess(
                        meal.getDateTime(),
                        meal.getDescription(),
                        meal.getCalories(),
                        excess));
            }
        }
        return mealsWithExcess;
    }

    public static List<UserMealWithExcess> filteredByStreams(
            List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {

        Map<LocalDate, Integer> dayCal = meals.stream()
                .collect(Collectors.groupingBy(
                        meal -> meal.getDateTime().toLocalDate(),
                        Collectors.summingInt(UserMeal::getCalories)));

        return meals.stream()
                .filter(meal -> isBetweenHalfOpen(meal.getDateTime().toLocalTime(), startTime, endTime))
                .map(meal -> new UserMealWithExcess(
                        meal.getDateTime(),
                        meal.getDescription(),
                        meal.getCalories(),
                        dayCal.get(meal.getDateTime().toLocalDate()) > caloriesPerDay))
                .collect(Collectors.toList());
    }

    public static List<UserMealWithExcess> filteredByCycle(
            List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> dayCal = new HashMap<>();
        List<UserMealWithExcess> mealsWithExcess = new ArrayList<>();

        for (UserMeal meal : meals) {
            dayCal.put(
                    meal.getDateTime().toLocalDate(),
                    dayCal.getOrDefault(meal.getDateTime().toLocalDate(),
                            0) + meal.getCalories());
        }

        for (UserMeal meal : meals) {

            if (isBetweenHalfOpen(meal.getDateTime().toLocalTime(), startTime, endTime)) {
                UserMealWithExcess mealWithExcess = new UserMealWithExcess(
                        meal.getDateTime(),
                        meal.getDescription(),
                        meal.getCalories(),
                        false);
                mealsWithExcess.add(mealWithExcess);
                reflectionExcessUpdate(mealWithExcess, dayCal, caloriesPerDay);
            }
        }
        return mealsWithExcess;
    }

    public static List<UserMealWithExcess> filteredByStream(
            List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {

        return meals.stream()
                .collect(Collectors.collectingAndThen(
                        Collectors.groupingBy(
                                meal -> meal.getDateTime().toLocalDate(),
                                Collectors.summingInt(UserMeal::getCalories)),
                dayCal -> meals.stream()
                .filter(meal -> isBetweenHalfOpen(meal.getDateTime().toLocalTime(), startTime, endTime))
                .map(meal -> new UserMealWithExcess(
                        meal.getDateTime(),
                        meal.getDescription(),
                        meal.getCalories(),
                        dayCal.get(meal.getDateTime().toLocalDate()) > caloriesPerDay))
                .collect(Collectors.toList())));
    }

    private static void reflectionExcessUpdate(UserMealWithExcess meal, Map<LocalDate, Integer> dayCal, int caloriesPerDay) {
        try {
            Field excessField = UserMealWithExcess.class.getDeclaredField("excess");
            excessField.setAccessible(true);
            boolean excess = dayCal.get(meal.getDateTime().toLocalDate()) > caloriesPerDay;
            excessField.set(meal, excess);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}