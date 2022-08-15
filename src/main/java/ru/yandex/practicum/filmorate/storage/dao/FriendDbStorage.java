package ru.yandex.practicum.filmorate.storage.dao;

import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FriendStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Value
@Slf4j
@Component("FriendDbStorage")
public class FriendDbStorage implements FriendStorage {

    JdbcTemplate jdbcTemplate;

    @Override
    public void addFriend(long reqUserId, long respUserId) {
        // Проверяем, был ли уже отправлен запрос на дружбу
        int affected = jdbcTemplate.update("UPDATE friends set req_user_id = ?, resp_user_id = ? " +
                "where req_user_id = ? AND resp_user_id = ?", respUserId, reqUserId, respUserId, reqUserId);
        System.out.println(affected);

        // Если нет, добавляем строку в таблицу
        String sqlQuery;
        if (affected == 0) {
            sqlQuery = "INSERT INTO friends (req_user_id, resp_user_id) VALUES (?, ?)";
            // Устанавливаем флаг подтверждения
        } else {
            sqlQuery = "UPDATE friends SET IS_FRIEND = 1 WHERE REQ_USER_ID = ? AND RESP_USER_ID = ?";
        }
        jdbcTemplate.update(sqlQuery, reqUserId, respUserId);
    }

    @Override
    public List<User> getAllFriends(long id) {
        String sqlQuery = "SELECT DISTINCT u.user_id, " +
                "                          u.name," +
                "                          u.LOGIN," +
                "                          u.email," +
                "                          u.birthday " +
                "FROM friends AS f " +
                "         INNER JOIN users AS u ON (u.user_id = f.REQ_USER_ID" +
                "    OR u.user_id = f.RESP_USER_ID) " +
                "    AND (f.REQ_USER_ID = ?" +
                "        OR f.RESP_USER_ID = ?)" +
                "WHERE ((f.RESP_USER_ID = ?" +
                "  AND f.is_friend) OR f.REQ_USER_ID = ?) AND u.USER_ID != ?" +
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
                "         INNER JOIN users AS u ON (u.user_id = f.REQ_USER_ID" +
                "    OR u.user_id = f.RESP_USER_ID)" +
                "    AND (f.REQ_USER_ID = ?" +
                "        OR f.RESP_USER_ID = ?" +
                "        OR f.REQ_USER_ID = ?" +
                "        OR f.RESP_USER_ID = ?)" +
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
        String sqlQuery = "DELETE FROM FRIENDS WHERE (REQ_USER_ID = ? AND RESP_USER_ID = ?) OR " +
                "(REQ_USER_ID = ? AND RESP_USER_ID = ?)";
        jdbcTemplate.update(sqlQuery, id, otherId, otherId, id);
        log.debug("Пользователь с id ? удалён из друзей пользователя с id ?", id, otherId);
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
