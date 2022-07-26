package ru.yandex.practicum.filmorate.service;

import lombok.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.List;

@Service
@Value
public class FilmService {

    FilmStorage filmStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public Film add(Film film) {
        return filmStorage.addFilm(film);
    }

    public Film update(Film film) {
        return filmStorage.updateFilm(film);
    }

    public List<Film> getFilms() {
        return filmStorage.getAllFilms();
    }

    public void delete(Film film) {
        filmStorage.deleteFilm(film);
    }


    public Film getFilm(long id) {
        return filmStorage.getFilm(id);
    }

    public Film addLike(long id, long userId) {
        return filmStorage.addLike(id, userId);
    }

    public Film removeLike(long id, long userId) {
        return filmStorage.removeLike(id, userId);
    }


    public List<Film> getPopular(int count) {
        return filmStorage.getPopular(count);
    }
}
