package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validator.FilmValidator;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage{

    private final List<Film> films = new ArrayList<>();
    private final FilmValidator validator = new FilmValidator();
    private static Long startId = 1L;

    @Override
    public Film addFilm(Film film) {
        log.info("Получен запрос на добавление нового фильмов");
        validator.validateReleaseDate(film);
        film.setId(startId++);
        films.add(film);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        log.info("Получен запрос на обновление фильма");
        validator.validateReleaseDate(film);
        Film update = films.stream()
                .filter(f -> f.getId() == film.getId())
                .findAny()
                .orElse(null);
        if (update != null) {
            films.remove(update);
            films.add(film);
            return film;
        } else {
            log.warn("Фильма с таким id={} нет - обновление не возможно", film.getId());
            throw new ValidationException("Нет фильма с таким id");
        }
    }

    @Override
    public Film deleteFilm(Film film) {
        return null;
    }

    @Override
    public List<Film> getAllFilms() {
        log.info("Получен запрос на получение списка фильмов");
        return films;
    }

}
