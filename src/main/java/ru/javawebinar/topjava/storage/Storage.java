package ru.javawebinar.topjava.storage;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;

public interface Storage {

    void save(Meal r); //

    Meal get(Integer uuid);

    void delete(Integer uuid);

    void update(Meal r, Integer uuid);

    List<Meal> getAllMeal();

}

