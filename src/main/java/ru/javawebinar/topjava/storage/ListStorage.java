package ru.javawebinar.topjava.storage;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicInteger;

import static org.slf4j.LoggerFactory.getLogger;

public class ListStorage implements Storage {
    private static final Logger log = getLogger(ListStorage.class);
    private final List<Meal> storage = new ArrayList<>();
    private final AtomicInteger nextUuid = new AtomicInteger(1);


    @Override
    public void save(Meal meal) {
        log.debug("meal saved");
        meal.setUuid(nextUuid.getAndIncrement());
        storage.add(meal);
    }

    @Override
    public Meal get(Integer uuid) {
        log.debug("meal retrieved");
        return storage.get(findIndexByUuid(uuid));
    }

    @Override
    public void update(Meal meal, Integer uuid) {
        log.debug("meal updated");
        int index = findIndexByUuid(uuid);
        meal.setUuid(uuid);
        storage.set(index, meal);
    }

    @Override
    public void delete(Integer uuid) {
        log.debug("meal deleted");
        storage.remove(findIndexByUuid(uuid));
    }

    @Override
    public List<Meal> getAllMeal() {
        log.debug("meal list retrieved");
        return new ArrayList<>(storage);
    }

    private int findIndexByUuid(Integer uuid) {
        for (int i = 0; i < storage.size(); i++) {
            if (storage.get(i).getUuid().equals(uuid)) {
                return i;
            }
        }
        throw new NoSuchElementException("Meal with uuid=" + uuid + " not found");
    }
}
