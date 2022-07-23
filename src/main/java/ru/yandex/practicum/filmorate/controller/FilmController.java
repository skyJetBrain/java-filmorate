package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/films")
public class FilmController {

    private final FilmStorage filmStorage;

    @Autowired
    public FilmController(FilmStorage filmStorage) {
        this.filmStorage = new InMemoryFilmStorage();
    }

    @GetMapping
    public List<Film> getFilms() {
        return filmStorage.getFilms();
    }

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {
        return filmStorage.addFilm(film);
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        return filmStorage.updateFilm(film);
    }



}
