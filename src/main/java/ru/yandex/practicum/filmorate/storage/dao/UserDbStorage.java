package ru.yandex.practicum.filmorate.storage.dao;

import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

@Value
@Slf4j
@Primary
@Component("UserDbStorage")
public class UserDbStorage implements UserStorage {
    JdbcTemplate jdbcTemplate;

    @Override
    public User add(User user) {
        String sqlQuery = "INSERT INTO USERS(NAME, LOGIN, EMAIL, BIRTHDAY) values ( ?, ?, ?, ? ) ";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"USER_ID"});
            stmt.setString(1, user.getName());
            stmt.setString(2, user.getLogin());
            stmt.setString(3, user.getEmail());
            stmt.setDate(4, Date.valueOf(user.getBirthday()));
            return stmt;
        }, keyHolder);
        user.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
        log.info("Пользователь с Id={} записан в базу данных", user.getId());
        return user;
    }

    @Override
    public User update(User user) {
        String sqlQuery = "UPDATE USERS SET  name = ?, login = ?, email = ?, birthday = ? WHERE USER_ID = ?";

        jdbcTemplate.update(sqlQuery,
                user.getName(),
                user.getLogin(),
                user.getEmail(),
                user.getBirthday(),
                user.getId());
        log.info("данные пользователя c Id={} обновлены в базе данных", user.getId());
        return get(user.getId());
    }

    @Override
    public void delete(User user) {
        String sqlQuery = "DELETE FROM USERS WHERE user_id = ?";
        log.info("Пользователь c Id={} удален из базы данных", user.getId());
        jdbcTemplate.update(sqlQuery, user.getId());
    }

    @Override
    public List<User> getAll() {
        String sqlQuery = "SELECT user_id, name, login, email, birthday FROM users";
        log.info("Получен запрос на получение списка пользователей из базы данных");
        return jdbcTemplate.query(sqlQuery, this::mapRowToUser);
    }

    @Override
    public User get(long id) {
        String sqlQuery = "SELECT user_id, name, login, email, birthday FROM users WHERE user_id = ?";
        int affected = jdbcTemplate.update("UPDATE users set user_id = ? where user_id = ?", id, id);
        if (affected == 0) {
            return null;
        }
        log.info("Получен запрос на получение из базы данных пользователя по его Id={} ", id);
        return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToUser, id);
    }

    @Override
    public User addFriend(long id, long friendId) {
        return null;
    }

    @Override
    public User removeFriend(long id, long friendId) {
        return null;
    }

    @Override
    public List<User> getFriends(long id) {
        return null;
    }

    @Override
    public List<User> getCommonFriends(long id, long otherId) {
        return null;
    }

    private User mapRowToUser(ResultSet resultSet, int id) throws SQLException {
        return User.builder()
                .id(resultSet.getLong("user_id"))
                .name(resultSet.getString("name"))
                .email(resultSet.getString("email"))
                .login(resultSet.getString("login"))
                .birthday(resultSet.getDate("birthday").toLocalDate())
                .build();
    }
}
