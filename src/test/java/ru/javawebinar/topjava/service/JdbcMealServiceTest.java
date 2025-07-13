package ru.javawebinar.topjava.service;

import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles({"jdbc", "hsqldb"})
public class JdbcMealServiceTest extends AbstractMealServiceTest{

}
