package ru.yandex.practicum.filmorate.storage.dao;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.List;

public class FilmDbStorage implements FilmStorage {
    @Override
    public Film add(Film film) {
        return null;
    }

    @Override
    public Film update(Film film) {
        return null;
    }

    @Override
    public void delete(Film film) {

    }

    @Override
    public List<Film> getAll() {
        return null;
    }

    @Override
    public Film get(long id) {
        return null;
    }

    @Override
    public Film addLike(long id, long userId) {
        return null;
    }

    @Override
    public Film removeLike(long id, long userId) {
        return null;
    }

    @Override
    public List<Film> getPopular(int count) {
        return null;
    }
}
