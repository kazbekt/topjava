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
    public synchronized void save(Meal meal) {
        log.debug("meal saved");
        meal.setUuid(nextUuid.getAndIncrement());
        storage.add(meal);
    }

    @Override
    public synchronized Meal get(String searchID) {
        log.debug("meal retrieved");
        return storage.get(findIndexByUuid(searchID));
    }

    @Override
    public synchronized void update(Meal meal) {
        log.debug("meal updated");
        if (meal.getUuid() == null) {
            throw new IllegalArgumentException("Meal uuid must not be null");
        }
        doUpdate(meal, meal.getUuid().toString());
    }

    @Override
    public synchronized void delete(String searchID) {
        log.debug("meal deleted");
        storage.remove(findIndexByUuid(searchID));
    }

    @Override
    public synchronized List<Meal> getAllMeal() {
        log.debug("meal list retrieved");
        return new ArrayList<>(storage);
    }

    private int findIndexByUuid(String searchID) {
        if (searchID == null) {
            throw new IllegalArgumentException("Uuid must not be null");
        }
        Integer uuid = Integer.parseInt(searchID);
        for (int i = 0; i < storage.size(); i++) {
            if (storage.get(i).getUuid().equals(uuid)) {
                return i;
            }
        }
        throw new NoSuchElementException("Meal with uuid=" + uuid + " not found");
    }

    private void doUpdate(Meal meal, String uuidString) {
        if (meal == null) {
            throw new IllegalArgumentException("Meal must not be null");
        }
        int index = findIndexByUuid(uuidString);
        storage.set(index, meal);
    }

}
