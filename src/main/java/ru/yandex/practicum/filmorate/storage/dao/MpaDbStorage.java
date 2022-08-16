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
        final String sqlQuery = "select MPA_ID ,NAME " +
                "FROM MPAS " +
                "where MPA_ID = ?";
        final List<Mpa> mpas = jdbcTemplate.query(sqlQuery, MpaDbStorage::makeMpa, id);
        if (mpas.size() != 1) {
            return null;
        }
        return mpas.get(0);
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

    static Mpa makeMpa(ResultSet rs, int rowNum) throws SQLException {
        return new Mpa(rs.getInt("MPA_ID"),
                rs.getString("NAME")
        );
    }
}
