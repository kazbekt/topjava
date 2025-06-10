package ru.javawebinar.topjava.storage;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;

public interface Storage {

    void save(Meal r); //

    Meal get(String searchID);

    void delete(String searchID);

    void update(Meal r);

    List<Meal> getAllMeal();

}

