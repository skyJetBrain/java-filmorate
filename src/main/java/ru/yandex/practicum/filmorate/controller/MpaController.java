package ru.yandex.practicum.filmorate.controller;

import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.util.List;

@Value
@RestController
@RequestMapping("/mpas")
public class MpaController {
    MpaStorage mpaStorage;

    @Autowired
    public MpaController(MpaStorage mpaStorage) {
        this.mpaStorage = mpaStorage;
    }

    @GetMapping()
    public List<Mpa> getAll() {
        return mpaStorage.getAll();
    }

    @GetMapping("{id}")
    public Mpa get(@PathVariable long id) {
        return mpaStorage.get(id);
    }
}
