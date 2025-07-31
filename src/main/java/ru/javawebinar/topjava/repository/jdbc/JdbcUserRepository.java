package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;
import ru.javawebinar.topjava.util.ValidationUtil;

import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Repository
@Transactional(readOnly = true)
public class JdbcUserRepository implements UserRepository {

    private static final BeanPropertyRowMapper<User> ROW_MAPPER = BeanPropertyRowMapper.newInstance(User.class);

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert insertUser;

    @Autowired
    public JdbcUserRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.insertUser = new SimpleJdbcInsert(jdbcTemplate).withTableName("users").usingGeneratedKeyColumns("id");

        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    @Transactional
    public User save(User user) {
        ValidationUtil.validate(user);
        if (user.isNew()) {
            Number newKey = insertUser.executeAndReturnKey(new BeanPropertySqlParameterSource(user));
            user.setId(newKey.intValue());
        } else {
            int updated = namedParameterJdbcTemplate.update("""
                    UPDATE users SET name=:name, email=:email, password=:password,
                    registered=:registered, enabled=:enabled, calories_per_day=:caloriesPerDay
                    WHERE id=:id""", new BeanPropertySqlParameterSource(user));
            if (updated == 0) return null;
            jdbcTemplate.update("DELETE FROM user_role WHERE user_id=?", user.getId());
        }
        saveRoles(user);
        return user;
    }

    @Override
    @Transactional
    public boolean delete(int id) {
        return jdbcTemplate.update("DELETE FROM users WHERE id=?", id) != 0;
    }

    @Override
    public User get(int id) {
        return queryForUserWithRoles("SELECT u.*, ur.role FROM users u LEFT JOIN user_role ur ON u.id = ur.user_id WHERE u.id = ?", id);
    }

    @Override
    public User getByEmail(String email) {
        return queryForUserWithRoles("SELECT u.*, ur.role FROM users u LEFT JOIN user_role ur ON u.id = ur.user_id WHERE u.email = ?", email);
    }

    @Override
    public List<User> getAll() {
        Map<Integer, User> userMap = new LinkedHashMap<>();

        jdbcTemplate.query("SELECT u.*, ur.role FROM users u LEFT JOIN user_role ur ON u.id = ur.user_id ORDER BY u.name, u.email", rs -> {
            int userId = rs.getInt("id");
            User user = userMap.computeIfAbsent(userId, id -> {
                try {
                    User u = ROW_MAPPER.mapRow(rs, 0);
                    u.setRoles(new HashSet<>());
                    return u;
                } catch (SQLException e) {
                    throw new DataAccessException("Error mapping user", e) {
                    };
                }
            });

            String role = rs.getString("role");
            if (role != null) {
                user.getRoles().add(Role.valueOf(role));
            }
        });

        return new ArrayList<>(userMap.values());
    }

    private void saveRoles(User user) {
        Set<Role> roles = user.getRoles();
        List<Object[]> batchArgs = roles.stream().map(role -> new Object[]{user.getId(), role.name()}).collect(Collectors.toList());

        jdbcTemplate.batchUpdate("INSERT INTO user_role (user_id, role) VALUES (?, ?)", batchArgs);
    }

    private User queryForUserWithRoles(String sql, Object... args) {
        return jdbcTemplate.query(sql, ps -> {
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }
        }, rs -> {
            User user = null;
            Set<Role> roles = new HashSet<>();
            while (rs.next()) {
                if (user == null) {
                    user = ROW_MAPPER.mapRow(rs, 0);
                }
                String role = rs.getString("role");
                if (role != null) {
                    roles.add(Role.valueOf(role));
                }
            }
            if (user != null) {
                user.setRoles(roles);
            }
            return user;
        });
    }
}


