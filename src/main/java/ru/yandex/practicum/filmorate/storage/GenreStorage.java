package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Set;

public interface GenreStorage {
    Genre get(long id);
    List<Genre> getAll();

    void set(Film films);

    Set<Genre> getFilmGenres(Film film);
}
