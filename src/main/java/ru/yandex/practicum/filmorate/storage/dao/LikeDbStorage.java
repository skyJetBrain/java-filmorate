package ru.yandex.practicum.filmorate.storage.dao;

import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.LikeStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Value
@Slf4j
@Component("LikeDbStorage")
public class LikeDbStorage implements LikeStorage {

    JdbcTemplate jdbcTemplate;

    @Override
    public void addLike(long filmId, long userId) {
        String sqlQuery = "INSERT INTO LIKES (FILM_ID, USER_ID) VALUES (?, ?)";
        jdbcTemplate.update(sqlQuery, filmId, userId);
        log.debug("Добавлен лайк фильму с id: {} пользователем с id: {}", filmId, userId);
    }

    @Override
    public void removeLike(long filmId, long userId) {
        String sqlQuery = "DELETE FROM LIKES WHERE FILM_ID = ? AND USER_ID = ?";
        jdbcTemplate.update(sqlQuery, filmId, userId);
        log.debug("Удалён лайк фильму с id: {} пользователем с id: {}", filmId, userId);
    }

    @Override
    public List<Film> getPopular(int count) {
        String sqlQuery = "SELECT f.FILM_ID, f.NAME , RELEASE_DATE , DESCRIPTION ,DURATION , MPAS.MPA_ID, MPAS.NAME " +
                "FROM FILMS f " +
                "LEFT JOIN LIKES l on f.FILM_ID = l.FILM_ID " +
                "JOIN MPAS ON MPAS.MPA_ID=f.MPA_ID " +
                "GROUP BY f.NAME " +
                "ORDER BY COUNT (l.USER_ID) DESC " +
                "LIMIT ?";
        return jdbcTemplate.query(sqlQuery, this::mapRowToFilm, count);
    }

    private Film mapRowToFilm(ResultSet resultSet, int rowNum) throws SQLException {
        return Film.builder()
                .id(resultSet.getLong("FILM_ID"))
                .name(resultSet.getString("NAME"))
                .releaseDate(resultSet.getDate("RELEASE_DATE").toLocalDate())
                .description(resultSet.getString("DESCRIPTION"))
                .duration(resultSet.getInt("DURATION"))
                .mpa(new Mpa(resultSet.getInt("MPAS.MPA_ID"), resultSet.getString("MPAS.NAME")))
                .build();
    }
}
