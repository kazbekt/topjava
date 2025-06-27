package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class MealTestData {
    public static final int UM_5_ID = START_SEQ + 5;
    public static final int UM_6_ID = START_SEQ + 6;
    public static final int UM_7_ID = START_SEQ + 7;
    public static final int UM_8_ID = START_SEQ + 8;
    public static final int UM_9_ID = START_SEQ + 9;
    public static final int UM_10_ID = START_SEQ + 10;
    public static final int UM_11_ID = START_SEQ + 11;

    public static final Meal um5 = new Meal(UM_5_ID, LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0, 0), "Завтрак", 500);
    public static final Meal um6 = new Meal(UM_6_ID, LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0, 0), "Обед", 1000);
    public static final Meal um7 = new Meal(UM_7_ID, LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0, 0), "Ужин", 500);
    public static final Meal um8 = new Meal(UM_8_ID, LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0, 0), "Еда на граничное значение", 100);
    public static final Meal um9 = new Meal(UM_9_ID, LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0, 0), "Завтрак", 1000);
    public static final Meal um10 = new Meal(UM_10_ID, LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0, 0), "Обед", 500);
    public static final Meal um11 = new Meal(UM_11_ID, LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0, 0), "Ужин", 410);

    public static final Meal newMeal = new Meal(null, LocalDateTime.of(2021, Month.JANUARY, 1, 10, 0), "newMeal", 555);
    public static final Meal updated =new Meal(UM_6_ID, LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обновленный обед", 1000);


    public static Meal getSameDateNewMeal() {
        return new Meal(null, um5.getDateTime(), "Новый завтрак", 1000);
    }

    public static void assertMatch(Meal actual, Meal expected) {
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    public static void assertMatch(Iterable<Meal> actual, Meal... expected) {
        assertMatch(actual, Arrays.asList(expected));
    }

    public static void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected) {
        assertThat(actual).isEqualTo(expected);
    }
}
