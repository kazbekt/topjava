package ru.javawebinar.topjava.service.jdbc;

import org.junit.Assume;
import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.service.AbstractUserServiceTest;

import static ru.javawebinar.topjava.Profiles.JDBC;

@ActiveProfiles(JDBC)
public class JdbcUserServiceTest extends AbstractUserServiceTest {

    @Override
    @Test
    public void createWithException() throws Exception {
        Assume.assumeFalse("Skipped for JDBC", true);
    }
}