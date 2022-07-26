package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {

    Film addFilm(Film film);

    Film updateFilm(Film film);

    void deleteFilm(Film film);

    List<Film> getAllFilms();

    Film getFilm(long id);

    Film addLike(long id, long userId);

    Film removeLike(long id, long userId);

    List<Film> getPopular(int count);
}
