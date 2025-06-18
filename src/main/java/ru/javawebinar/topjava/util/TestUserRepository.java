package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;
import ru.javawebinar.topjava.repository.inmemory.InMemoryUserRepository;

import java.util.Arrays;
import java.util.List;

public class TestUserRepository {

    public static final User testUser = new User(
            null, "Test Test", "test", "pass",
            MealsUtil.DEFAULT_CALORIES_PER_DAY, true,
            Arrays.asList(Role.USER, Role.ADMIN));

    public static final List<User> users = Arrays.asList(
            new User(null, "Ivan Ivanov", "email2", "pass", Role.USER),
            new User(null, "Ivan Ivanov", "email1", "pass", Role.USER),
            new User(null, "Petr Petrov", "email3", "pass", Role.USER),
            new User(null, "Sergei Serge", "email4", "pass", Role.USER),
            new User(null, "Pavel Pavlov", "email5", "pass", Role.USER)
    );

    public static void main(String[]args) {
        UserRepository repository = new InMemoryUserRepository();
        repository.getAll().forEach(System.out::println);
        repository.delete(4);
        repository.getAll().forEach(System.out::println);
        repository.save(testUser);
        repository.getAll().forEach(System.out::println);
        System.out.println(repository.getByEmail("test"));
    }
}
