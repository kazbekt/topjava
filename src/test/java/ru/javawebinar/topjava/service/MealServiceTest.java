package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.util.List;

import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.*;

import static org.junit.Assert.*;


@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {
    static {
        // Only for postgres driver logging
        // It uses java.util.logging and logged via jul-to-slf4j bridge
        SLF4JBridgeHandler.install();
    }

    @Autowired
    private MealService service;

    @Test
    public void get() {
        Meal meal = service.get(UM_5_ID, USER_ID);
        assertMatch(meal, um5);
    }

    @Test
    public void delete() {
        service.delete(UM_5_ID, USER_ID);
        assertThrows(NotFoundException.class, () -> service.get(UM_5_ID, USER_ID));
    }

    @Test
    public void getBetweenInclusive() {
        List<Meal> meals = service.getBetweenInclusive(um6.getDateTime().toLocalDate(), um7.getDateTime().toLocalDate(), USER_ID);
        assertMatch(meals, um7, um6, um5);
    }

    @Test
    public void getAll() {
        List<Meal> all = service.getAll(USER_ID);
        assertMatch(all, um11, um10, um9, um8, um7, um6, um5);
    }

    @Test
    public void update() {
        service.update(updated, USER_ID);
        Meal actual = service.get(UM_6_ID, USER_ID);
        assertMatch(actual, updated);
    }

    @Test
    public void create() {
        Meal created = service.create(newMeal, USER_ID);
        newMeal.setId(created.getId());
        MealTestData.assertMatch(created, newMeal);
        MealTestData.assertMatch(service.get(created.getId(), USER_ID), newMeal);
    }

    @Test
    public void createSameDateNewMeal() {
        Meal sameDateNewMeal = MealTestData.getSameDateNewMeal();
        assertThrows(DuplicateKeyException.class, () -> service.create(sameDateNewMeal, USER_ID));
    }

    @Test
    public void deleteNotFound() {
        assertThrows(NotFoundException.class, () -> service.delete(UM_5_ID, ADMIN_ID));
    }

    @Test
    public void getNotFound() {
        assertThrows(NotFoundException.class, () -> service.get(UM_5_ID, ADMIN_ID));
    }

    @Test
    public void updateNotFound() {
        assertThrows(NotFoundException.class, () -> service.update(um5, ADMIN_ID));
    }
}