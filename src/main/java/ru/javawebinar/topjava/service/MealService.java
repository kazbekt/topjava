package ru.javawebinar.topjava.service;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;

import java.util.Collection;

import static ru.javawebinar.topjava.util.ValidationUtil.*;

public class MealService {

    private final MealRepository repository;

    public MealService(MealRepository repository) {
        this.repository = repository;
    }

    public Meal create(Meal meal, int authUserId) {
        checkIsNew(meal);
        return repository.save(meal, authUserId);
    }

    public void delete(int id, int authUserId) {
    checkNotFound(repository.delete(id, authUserId), id);
    }

    public Meal get(int id, int authUserId) {
        return checkNotFound(repository.get(id, authUserId), id);
    }

    public void update(Meal meal, int id, int authUserId) {
        assureIdConsistent(meal, id);
        checkNotFound(repository.save(meal, authUserId), id);
    }

    public Collection<Meal> getAll() {
        return repository.getAll();
    }

}