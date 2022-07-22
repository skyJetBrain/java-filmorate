package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.List;

@Component
public class InMemoryFilmStorage implements FilmStorage{

    List<Film> films = new ArrayList<>();

    @Override
    public Film addFilm(Film film) {
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        return null;
    }

    @Override
    public Film deleteFilm(Film film) {
        return null;
    }

    @Override
    public List<Film> getAllFilms() {
        return films;
    }

}
