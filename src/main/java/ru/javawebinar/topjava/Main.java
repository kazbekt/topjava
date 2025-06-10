package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.storage.ListStorage;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDateTime;
import java.time.Month;

/**
 * @see <a href="http://topjava.herokuapp.com">Demo application</a>
 * @see <a href="https://github.com/JavaOPs/topjava">Initial project</a>
 */
public class Main {
    public static void main(String[] args) {
        System.out.format("Hello TopJava Enterprise!");

        ListStorage storage = new ListStorage();
        MealsUtil.meals.forEach(m -> storage.save(m));
        System.out.println("Storage created: ");
        storage.getAllMeal().forEach(m -> System.out.println(m));
        System.out.println("Storage get meal: ");
        System.out.println(storage.get(4));
        System.out.println("Storage delete meal: ");
        storage.delete(4);
        storage.getAllMeal().forEach(m -> System.out.println(m));
        System.out.println("Storage update meal: ");
        storage.update(new Meal(
                LocalDateTime.of(2020, Month.JANUARY, 31, 21, 0),
                "Свежий ужин",
                510), 7);
        storage.getAllMeal().forEach(m -> System.out.println(m));
    }
}
