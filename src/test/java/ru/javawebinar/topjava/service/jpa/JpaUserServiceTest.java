package ru.javawebinar.topjava.service.jpa;

import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.ActiveDbProfileResolver;
import ru.javawebinar.topjava.service.AbstractUserServiceTest;
import static ru.javawebinar.topjava.Profiles.JPA;

@ActiveProfiles(
        resolver = ActiveDbProfileResolver.class,
        profiles = JPA
)
public class JpaUserServiceTest extends AbstractUserServiceTest {
    @Override
    public void setup() {
        super.setup();
    }

    @Override
    public void create() {
        super.create();
    }

    @Override
    public void duplicateMailCreate() {
        super.duplicateMailCreate();
    }

    @Override
    public void delete() {
        super.delete();
    }

    @Override
    public void deletedNotFound() {
        super.deletedNotFound();
    }

    @Override
    public void get() {
        super.get();
    }

    @Override
    public void getNotFound() {
        super.getNotFound();
    }

    @Override
    public void getByEmail() {
        super.getByEmail();
    }

    @Override
    public void update() {
        super.update();
    }

    @Override
    public void getAll() {
        super.getAll();
    }
}
