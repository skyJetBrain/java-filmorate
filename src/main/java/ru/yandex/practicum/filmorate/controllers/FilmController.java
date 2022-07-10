package ru.yandex.practicum.filmorate.controllers;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/films")
public class FilmController {

    private final Map<Integer, Film> films = new LinkedHashMap<>();

    @GetMapping
    public Map<Integer, Film> getFilms() {
        return films;
    }

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {
        films.put(film.getId(), film);
        return film;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        films.put(film.getId(), film);
        return film;
    }

}
