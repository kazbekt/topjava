package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import static ru.javawebinar.topjava.Profiles.HSQL_DB;
import static ru.javawebinar.topjava.util.DateTimeUtil.localDateTimeToTimestamp;

@Repository
@Profile(HSQL_DB)
public class JdbcMealRepositoryHsqldb extends AbstractJdbcMealRepository<Timestamp> {

    @Autowired
    public JdbcMealRepositoryHsqldb(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        super(jdbcTemplate, namedParameterJdbcTemplate);
    }

    protected Timestamp convertDateTime(LocalDateTime dateTime) {
        return localDateTimeToTimestamp(dateTime);
    }

}
