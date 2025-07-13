package ru.javawebinar.topjava.service;

import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles({"datajpa", "hsqldb"})
public class DataJpaMealServiceTest extends AbstractMealServiceTest{

}
