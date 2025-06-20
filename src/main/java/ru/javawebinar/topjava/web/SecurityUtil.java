package ru.javawebinar.topjava.web;

import static ru.javawebinar.topjava.util.MealsUtil.DEFAULT_CALORIES_PER_DAY;

public class SecurityUtil {

    private static final ThreadLocal<Integer> authUserId = new ThreadLocal<>();

    public static void setAuthUserId(int id) {
        authUserId.set(id);
    }

    public static int authUserId() {
        Integer id = authUserId.get();
        return id != null ? id : 1;
    }

    public static int authUserCaloriesPerDay() {
        return DEFAULT_CALORIES_PER_DAY;
    }
}