package ru.yandex.practicum.filmorate.storage.dao;

import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

@Value
@Slf4j
@Repository
@Primary
public class FilmDbStorage implements FilmStorage {

    JdbcTemplate jdbcTemplate;
    @Override
    public Film add(Film film) {
        String sqlQuery = "INSERT INTO FILMS (NAME, DESCRIPTION, RELEASE_DATE, DURATION, MPA_ID) " +
                "VALUES (?, ?, ?, ?, ?) ";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"FILM_ID"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setDate(3, Date.valueOf(film.getReleaseDate()));
            stmt.setLong(4, film.getDuration());
            stmt.setLong(5, film.getMpa().getId());
            return stmt;
        }, keyHolder);
        film.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
        log.info("Фильм с Id={} записан в базу данных", film.getId());
        return film;
    }

    @Override
    public Film update(Film film) {
        String sqlQuery = "UPDATE FILMS SET  name = ?, DESCRIPTION = ?, RELEASE_DATE = ?, DURATION = ?," +
                " MPA_ID = ?  WHERE FILM_ID = ?";

        jdbcTemplate.update(sqlQuery,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId()
        );
        log.debug("данные фильма с Id={} обновлены в базе данных", film.getId());
        return get(film.getId());
    }

    @Override
    public void delete(Film film) {
        String sqlQuery = "DELETE FROM FILMS WHERE FILM_ID = ?";
        log.info("Фильм c Id={} удален из базы данных", film.getId());
        jdbcTemplate.update(sqlQuery, film.getId());
    }

    @Override
    public List<Film> getAll() {
        String sqlQuery = "SELECT f.FILM_ID, f.NAME, f.DESCRIPTION, f.RELEASE_DATE, f.DURATION, f.MPA_ID, m.NAME " +
                "FROM FILMS AS f " +
                "INNER JOIN MPAS AS m on m.MPA_ID = f.MPA_ID " +
                "GROUP BY f.FILM_ID";
        log.info("Получен запрос на получение списка фильмов из базы данных");
        return jdbcTemplate.query(sqlQuery, this::mapRowToFilm);
    }

    @Override
    public Film get(long id) {
        String sqlQuery = "SELECT f.FILM_ID, f.NAME, f.DESCRIPTION, f.RELEASE_DATE, f.DURATION, f.MPA_ID, m.NAME " +
                "FROM FILMS AS f " +
                "INNER JOIN MPAS m on m.MPA_ID = f.MPA_ID AND f.FILM_ID = ?";

        int affected = jdbcTemplate.update("UPDATE FILMS set FILM_ID = ? where FILM_ID = ?", id, id);
        if (affected == 0) {
            return null;
        }
        log.info("Получен запрос на получение из базы данных фильма по его Id={} ", id);
        return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToFilm, id);
    }

    @Override
    public Film addLike(long id, long userId) {
        return null;
    }

    @Override
    public Film removeLike(long id, long userId) {
        return null;
    }

    @Override
    public List<Film> getPopular(int count) {
        return null;
    }

    private Film mapRowToFilm(ResultSet resultSet, int rowNum) throws SQLException {
        return Film.builder()
                .id(resultSet.getLong("film_id"))
                .name(resultSet.getString("name"))
                .releaseDate(resultSet.getDate("release_date").toLocalDate())
                .description(resultSet.getString("description"))
                .duration(resultSet.getInt("duration"))
                .mpa(new Mpa(resultSet.getInt("MPAS.MPA_ID"), resultSet.getString("MPAS.NAME")))
                .build();
    }
}
