package ru.yandex.practicum.filmorate.controller;


import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;
import ru.yandex.practicum.filmorate.storage.dao.GenreDbStorage;

import java.util.List;

@Value
@Slf4j
@RestController
@RequestMapping("/genres")
public class GenreController {

    GenreStorage genreStorage;

    @Autowired
    public GenreController(GenreStorage genreStorage) {
        this.genreStorage = genreStorage;
    }

    @GetMapping()
    public List<Genre> getAllGenres() {
        log.debug("Общее количество жанров в справочнике : {}", genreStorage.getAll().size());
        return genreStorage.getAll();
    }



    @GetMapping("{id}")
    public Genre getGenreById(@PathVariable long id) {
        if (id > 6 || id < 1) {
            throw new NotFoundException("Genre with id=" + id + "not found");
        }
        log.debug("Get genre by id={}", id);
        return genreStorage.get(id);
    }
}
