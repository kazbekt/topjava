package ru.javawebinar.topjava.service.datajpa;

import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.service.AbstractMealServiceTest;
import static ru.javawebinar.topjava.Profiles.DATAJPA;

@ActiveProfiles(profiles = DATAJPA)
public class DataJpaMealServiceTest extends AbstractMealServiceTest {
}
