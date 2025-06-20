package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.repository.inmemory.InMemoryMealRepository;
import ru.javawebinar.topjava.to.MealTo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class MealsUtil {
    public static final int DEFAULT_CALORIES_PER_DAY = 2000;
    public static final int USER_ID = 1;
    public static final int WRONG_USER_ID = 2;

    public static final List<Meal> meals = Arrays.asList(
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500, USER_ID),
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000, USER_ID),
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500, USER_ID),
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100, USER_ID),
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000, USER_ID),
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500, USER_ID),
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410, USER_ID),
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Неправильная еда", 410, WRONG_USER_ID)
    );


    public static void main(String[] args) {
        MealRepository repository = new InMemoryMealRepository();
        MealService service = new MealService(repository);

        getTos(service.getAll(USER_ID), DEFAULT_CALORIES_PER_DAY).forEach(System.out::println);
        service.delete(4, USER_ID);

        System.out.println("After delete:");
        getTos(service.getAll(USER_ID), DEFAULT_CALORIES_PER_DAY).forEach(System.out::println);
        System.out.println("After save meal with userId == null:");
        service.create(new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Новая еда", 1000, null), 1);
        getTos(service.getAll(USER_ID), DEFAULT_CALORIES_PER_DAY).forEach(System.out::println);

    }

    public static List<MealTo> getTos(Collection<Meal> meals, int caloriesPerDay) {
        return filterByPredicate(meals, caloriesPerDay, meal -> true);
    }

    public static List<MealTo> getFilteredTos(Collection<Meal> meals,
                                              int caloriesPerDay,
                                              LocalTime startTime,
                                              LocalTime endTime) {
        return filterByPredicate(
                meals,
                caloriesPerDay,
                meal -> DateTimeUtil.isBetweenHalfOpen(meal.getTime(), startTime, endTime));
    }

    private static List<MealTo> filterByPredicate(Collection<Meal> meals, int caloriesPerDay, Predicate<Meal> filter) {
        Map<LocalDate, Integer> caloriesSumByDate = meals.stream()
                .collect(
                        Collectors.groupingBy(Meal::getDate, Collectors.summingInt(Meal::getCalories))
//                      Collectors.toMap(Meal::getDate, Meal::getCalories, Integer::sum)
                );

        return meals.stream()
                .filter(filter)
                .map(meal -> createTo(meal, caloriesSumByDate.get(meal.getDate()) > caloriesPerDay))
                .collect(Collectors.toList());
    }

    private static MealTo createTo(Meal meal, boolean excess) {
        return new MealTo(meal.getId(), meal.getDateTime(), meal.getDescription(), meal.getCalories(), excess);
    }

}
