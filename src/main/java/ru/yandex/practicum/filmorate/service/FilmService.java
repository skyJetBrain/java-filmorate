package ru.yandex.practicum.filmorate.service;

import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.GenreStorage;
import ru.yandex.practicum.filmorate.storage.LikeStorage;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Value
@Slf4j
public class FilmService {

    FilmStorage filmStorage;
    LikeStorage likeStorage;
    GenreStorage genreStorage;
    MpaStorage mpaStorage;
    UserService userService;


    @Autowired
    public FilmService(@Qualifier("FilmDbStorage") FilmStorage filmStorage, LikeStorage likeStorage, GenreStorage genreStorage, MpaStorage mpaStorage, UserService userService) {
        this.filmStorage = filmStorage;
        this.likeStorage = likeStorage;
        this.genreStorage = genreStorage;
        this.mpaStorage = mpaStorage;
        this.userService = userService;
    }


    public Film add(Film film) {
        final Film filmFromStorage = filmStorage.get(film.getId());
        if (filmFromStorage == null) {
            filmStorage.add(film);
            genreStorage.set(film);//записываем жанры фильму,заполняем таблицу FILM_GENRES
        } else throw new ValidationException(String.format(
                "Фильм с таким id %s уже зарегистрирован.", film.getId()));
        return film;
    }

    public Film update(Film film) {
        if (filmStorage.get(film.getId()) != null) {
            log.debug("Обновлён фильм: {}", film);

        } else {
            throw new NotFoundException("Фильм не найден");
        }
        genreStorage.set(film);
        film = filmStorage.update(film);
        film.setGenres(genreStorage.getFilmGenres(film));
        return film;
    }

    public List<Film> getFilms() {
        List<Film> films = filmStorage.getAll();
        return films.stream()
                .peek(genreStorage::set)
                .collect(Collectors.toList());
    }

    public void delete(Film film) {
        filmStorage.delete(film);
    }


    public Film getFilm(long id) {
        final Film film = filmStorage.get(id);
        if (film == null) {
            throw new NotFoundException("User with id=" + id + "not found");
        }
        film.setGenres(new HashSet<>(genreStorage.getFilmGenres(film))); //получаем жанры фильма и добавляем к обьекту
        return film;
    }

    public Film addLike(long id, long userId) {
        if (userService.getUserStorage().get(userId) == null) {
            throw new NotFoundException("Пользователь не найден");
        }
        if (filmStorage.get(id) == null) {
            throw new NotFoundException("Фильм не найден");
        }
        likeStorage.addLike(id, userId);
        return filmStorage.get(id);
    }

    public Film removeLike(long id, long userId) {
        if (userService.getUserStorage().get(userId) == null) {
            throw new NotFoundException("Пользователь не найден");
        }
        if (filmStorage.get(id) == null) {
            throw new NotFoundException("Фильм не найден");
        }
        likeStorage.removeLike(id, userId);
        return filmStorage.get(id);
    }

    public List<Film> getPopular(int count) {
        List<Long> mostPopularFilmsId = likeStorage.getPopular(count);
        return mostPopularFilmsId.stream()
                .map(filmStorage::get)
                .collect(Collectors.toList());
    }

    public List<Genre> getAllGenres() {
        return genreStorage.getAll();
    }

    public Genre getGenreById(long id) {
        if (genreStorage.get(id) == null) {
            throw new NotFoundException("Жанр не найден");
        }
        return genreStorage.get(id);
    }

    public List<Mpa> getAllMpa() {
        return mpaStorage.getAll();
    }

    public Mpa getMpaById(long id) {
        if (mpaStorage.get(id) == null) {
            throw new NotFoundException("Рейтинг не найден");
        }
        return mpaStorage.get(id);
    }
}
