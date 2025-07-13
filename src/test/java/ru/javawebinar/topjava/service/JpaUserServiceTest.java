package ru.javawebinar.topjava.service;

import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles({"jpa", "hsqldb"})
public class JpaUserServiceTest extends AbstractUserServiceTest{

}
