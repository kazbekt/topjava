package ru.javawebinar.topjava.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

public class UserMealWithExcess {
    private final LocalDateTime dateTime;

    private final String description;

    private final int calories;

    private final Map<LocalDate, Integer> dayCal;

    private final int caloriesPerDay;

    public UserMealWithExcess(LocalDateTime dateTime, String description, int calories
    ,Map<LocalDate, Integer> dayCal, int caloriesPerDay) {
        this.dateTime = dateTime;
        this.description = description;
        this.calories = calories;
        this.dayCal = dayCal;
        this.caloriesPerDay = caloriesPerDay;
    }

    @Override
    public String toString() {
        return "UserMealWithExcess{" +
                "dateTime=" + dateTime +
                ", description='" + description + '\'' +
                ", calories=" + calories +
                ", excess=" + isExcess() +
                '}';
    }

    private boolean isExcess() {
        Integer totalDailyCalories = dayCal.get(this.dateTime.toLocalDate());
        return totalDailyCalories != null && totalDailyCalories > caloriesPerDay;
    }
}