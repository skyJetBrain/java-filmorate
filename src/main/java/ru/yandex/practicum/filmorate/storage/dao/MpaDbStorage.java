package ru.yandex.practicum.filmorate.storage.dao;

import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Value
@Slf4j
@Component("MpaDbStorage")
public class MpaDbStorage implements MpaStorage {

    JdbcTemplate jdbcTemplate;
    @Override
    public Mpa get(long id) {
        int affected = jdbcTemplate.update("UPDATE MPAS set MPA_ID = ? where MPA_ID = ?", id, id);
        if (affected == 0) {
            return null;
        }
        String sqlQuery = "SELECT * FROM MPAS WHERE MPA_ID = ?";
        return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToMpa, id);
    }

    @Override
    public List<Mpa> getAll() {
        String sqlQuery = "SELECT * FROM MPAS";
        List<Mpa> mpas = jdbcTemplate.query(sqlQuery, this::mapRowToMpa);
        log.debug("Получены рейтинги из базы данных {}", mpas);
        return mpas;
    }

    private Mpa mapRowToMpa(ResultSet resultSet, int id) throws SQLException {
        log.info("Получен рейтинг c id= {} из базы данных", id);
        return new Mpa(resultSet.getLong("mpa_id"), resultSet.getString("name"));
    }
}
