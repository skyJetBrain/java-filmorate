package ru.yandex.practicum.filmorate.storage.dao;

import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FriendStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Value
@Slf4j
@Repository
public class FriendDbStorage implements FriendStorage {

    JdbcTemplate jdbcTemplate;

    @Override
    public void addFriend(long fromUserId, long toUserId) {
        int affected = jdbcTemplate.update("UPDATE friends set USER_FROM_ID = ?, USER_TO_ID = ? " +
                "where USER_FROM_ID = ? AND USER_TO_ID = ?", fromUserId, toUserId, fromUserId, toUserId);

        String sqlQuery;
        if (affected == 0) {
            sqlQuery = "INSERT INTO friends (USER_FROM_ID, USER_TO_ID) VALUES (?, ?)";
        } else {
            sqlQuery = "UPDATE friends SET IS_CONFIRMED = 1 WHERE USER_FROM_ID = ? AND USER_TO_ID = ?";
        }
        jdbcTemplate.update(sqlQuery, fromUserId, toUserId);
    }

    @Override
    public List<User> getAllFriends(long id) {
        String sqlQuery = "SELECT DISTINCT u.user_id, " +
                "                          u.name," +
                "                          u.LOGIN," +
                "                          u.email," +
                "                          u.birthday " +
                "FROM friends AS f " +
                "         INNER JOIN users AS u ON (u.user_id = f.USER_TO_ID" +
                "    OR u.user_id = f.USER_FROM_ID) " +
                "    AND (f.USER_TO_ID = ?" +
                "        OR f.USER_FROM_ID = ?)" +
                "WHERE ((f.USER_FROM_ID = ?" +
                "  AND f.IS_CONFIRMED) OR f.USER_TO_ID = ?) AND u.USER_ID != ?" +
                "ORDER BY u.user_id";
        List<User> users = jdbcTemplate.query(sqlQuery, this::mapRowToUser, id, id, id, id, id);
        log.debug("Выгружен список друзей пользователя с id {}, количество друзей: {}", id, users.size());
        return users;
    }

    @Override
    public List<User> getCommonFriends(long id, long otherId) {
        String sqlQuery = "SELECT DISTINCT u.user_id," +
                "                          u.name," +
                "                          u.email," +
                "                          u.login," +
                "                          u.birthday " +
                "FROM friends AS f" +
                "         INNER JOIN users AS u ON (u.user_id = f.USER_TO_ID" +
                "    OR u.user_id = f.USER_FROM_ID)" +
                "    AND (f.USER_TO_ID = ?" +
                "        OR f.USER_FROM_ID = ?" +
                "        OR f.USER_TO_ID = ?" +
                "        OR f.USER_FROM_ID = ?)" +
                "WHERE u.user_id != ?" +
                "  AND u.user_id != ?" +
                "GROUP BY u.user_id " +
                "ORDER BY u.user_id";
        List<User> users = jdbcTemplate.query(sqlQuery, this::mapRowToUser, id, id, otherId, otherId, id, otherId);
        log.debug("Выгружен список общих друзей для пользователей с id {} и {}, количестов друзей: {}",
                id, otherId, users.size());
        return users;
    }

    @Override
    public void deleteFromFriends(long id, long otherId) {
        String sqlQuery = "DELETE FROM FRIENDS WHERE (USER_TO_ID = ? AND USER_FROM_ID = ?) OR " +
                "(USER_TO_ID = ? AND USER_FROM_ID = ?)";
        jdbcTemplate.update(sqlQuery, id, otherId, otherId, id);
        log.debug("Пользователь с id={} удалён из друзей пользователя с id={}", id, otherId);
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
