package ru.yandex.practicum.filmorate.storage.dao;

import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
@Value
@Slf4j
@Repository
public class GenreDbStorage implements GenreStorage {
    JdbcTemplate jdbcTemplate;

    @Override
    public Genre get(long id) {
        int affected = jdbcTemplate.update("UPDATE GENRES set GENRE_ID = ? where GENRE_ID = ?", id, id);
        if (affected == 0) {
            return null;
        }
        String sqlQuery = "SELECT * FROM GENRES WHERE GENRE_ID = ?";
        return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToGenre, id);
    }

    @Override
    public List<Genre> getAll() {
        String sqlQuery = "SELECT * FROM GENRES";
        List<Genre> genres = jdbcTemplate.query(sqlQuery, this::mapRowToGenre);
        log.info("Запрос на полукчение всех жанров из базы данных ");
        return genres;
    }


    @Override
    public void set(Film film) {
        String sqlQuery = "SELECT g.GENRE_ID," +
                "                 g.NAME " +
                "FROM FILMS_GENRES AS fg " +
                "INNER JOIN GENRES g on g.GENRE_ID = FG.GENRE_ID " +
                "WHERE fg.FILM_ID = ?";
        List<Genre> genres = jdbcTemplate.query(sqlQuery, this::mapRowToGenre, film.getId());
        Set<Genre> genresSet = new HashSet<>(genres);
        film.setGenres(genresSet);

    }

    @Override
    public Set<Genre> getFilmGenres(Film film) {
        Set<Genre> genres;
        String sqlQuery = "SELECT g.GENRE_ID, g.NAME " +
                "FROM FILMS_GENRES AS fg " +
                "INNER JOIN GENRES g on g.GENRE_ID = fg.GENRE_ID " +
                "WHERE fg.FILM_ID = ?";
        genres = new HashSet<>(jdbcTemplate.query(sqlQuery, this::mapRowToGenre, film.getId()));
        return genres;
    }

    private Genre mapRowToGenre(ResultSet resultSet, int rowNum) throws SQLException {
        return Genre.builder()
                .id(resultSet.getInt("GENRE_ID"))
                .name(resultSet.getString("NAME"))
                .build();
    }
}
