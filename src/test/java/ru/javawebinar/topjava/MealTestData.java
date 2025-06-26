package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class MealTestData {
    public static final int UM_5_ID = 100005;
    public static final int UM_6_ID = 100006;
    public static final int UM_7_ID = 100007;
    public static final int UM_8_ID = 100008;
    public static final int UM_9_ID = 100009;
    public static final int UM_10_ID = 100010;
    public static final int UM_11_ID = 100011;

    public static final Meal UM_5 = new Meal(UM_5_ID, LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0, 0), "Завтрак", 500);
    public static final Meal UM_6 = new Meal(UM_6_ID, LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0, 0), "Обед", 1000);
    public static final Meal UM_7 = new Meal(UM_7_ID, LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0, 0), "Ужин", 500);
    public static final Meal UM_8 = new Meal(UM_8_ID, LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0, 0), "Еда на граничное значение", 100);
    public static final Meal UM_9 = new Meal(UM_9_ID, LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0, 0), "Завтрак", 1000);
    public static final Meal UM_10 = new Meal(UM_10_ID, LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0, 0), "Обед", 500);
    public static final Meal UM_11 = new Meal(UM_11_ID, LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0, 0), "Ужин", 410);

    public static Meal getNew() {
        return new Meal(null, LocalDateTime.of(2021, Month.JANUARY, 1, 10, 0), "newMeal", 555);
    }

    public static Meal getUpdated() {
        return new Meal(UM_6_ID, LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обновленный обед", 1000);
    }

    public static Meal getSameDateNewMeal() {
        return new Meal(null, LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Новый завтрак", 1000);
    }

    public static void assertMatch(Meal actual, Meal expected) {
        assertThat(actual).usingRecursiveComparison().ignoringFields("registered", "roles").isEqualTo(expected);
    }

    public static void assertMatch(Iterable<Meal> actual, Meal... expected) {
        assertMatch(actual, Arrays.asList(expected));
    }

    public static void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected) {
        assertThat(actual).usingRecursiveFieldByFieldElementComparatorIgnoringFields("registered", "roles").isEqualTo(expected);
    }
}
