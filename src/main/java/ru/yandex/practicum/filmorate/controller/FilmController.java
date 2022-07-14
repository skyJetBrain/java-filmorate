package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validator.FilmValidator;

import javax.validation.Valid;
import java.util.*;

@RestController
@Slf4j
@RequestMapping("/films")
public class FilmController {

    private final List<Film> films = new ArrayList<>();
    private final FilmValidator validator = new FilmValidator();
    private static int startId = 1;

    @GetMapping
    public List<Film> getFilms() {
        log.info("Получен запрос на получение списка фильмов");
        return films;
    }

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {
        log.info("Получен запрос на добавление нового фильмов");
        validator.validateReleaseDate(film);
        film.setId(startId++);
        films.add(film);
        return film;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
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
            log.warn("Фильма с таким id нет - обновление не возможно");
            throw new ValidationException("Нет фильма с таким id");
        }
    }



}
