package ru.javawebinar.topjava.service;

import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles({"jpa", "hsqldb"})
public class JpaMealServiceTest extends AbstractMealServiceTest{

}
