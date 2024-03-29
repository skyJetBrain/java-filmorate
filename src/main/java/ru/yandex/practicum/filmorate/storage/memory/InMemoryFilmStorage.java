package ru.yandex.practicum.filmorate.storage.memory;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.validator.FilmValidator;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {

    private final List<Film> films = new ArrayList<>();
    private final FilmValidator validator = new FilmValidator();
    private static long startId = 1;

    @Override
    public Film add(Film film) {
        log.info("Получен запрос на добавление нового фильмов");
        validator.validateReleaseDate(film);
        film.setId(startId++);
        films.add(film);
        return film;
    }

    @Override
    public Film update(Film film) {
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
            throw new NotFoundException("Нет фильма с таким id=" + film.getId());
        }
    }

    @Override
    public void delete(Film film) {
        Film delete = films.stream()
                .filter(f -> f.getId() == film.getId())
                .findAny()
                .orElse(null);
        films.remove(delete);
    }

    @Override
    public List<Film> getAll() {
        log.info("Получен запрос на получение списка фильмов");
        return films;
    }

    @Override
    public Film get(long id) {
        log.info("Получен запрос на получение фильма по его Id");
        return checkFilmById(id);
    }



    @Override
    public Film addLike(long id, long userId) {
        log.info("Получен запрос на добавление лайка фильму");
        Film filmToLike = checkFilmById(id);

        filmToLike.addLike(userId);
        return filmToLike;

    }

    @Override
    public Film removeLike(long id, long userId) {
        log.info("Получен запрос на удаление лайка у фильма");
        Film filmToUnLike = checkFilmById(id);
        Set<Long> likes = filmToUnLike.getLikes();
        if (!likes.contains(userId)) {
            throw new NotFoundException("Не найдено пользователя поставившего лайк");
        }

        filmToUnLike.removeLike(userId);
        return filmToUnLike;
    }

    @Override
    public List<Film> getPopular(int count) {
        log.info("Получен запрос на получение списка популярных фильмов");
        films.sort(Comparator.comparingInt(Film::getLikesCount).reversed());
        return films.subList(0, Math.min(count, films.size()));
    }

    public Film checkFilmById(long id) {
        return films.stream()
                .filter(f -> id == f.getId())
                .findFirst()
                .orElseThrow(() -> {
                    log.warn("Фильма с таким id={} нет - удаление лайка не возможно", id);
                    throw new NotFoundException("Нет фильма с таким id=" + id);
                });
    }


}
