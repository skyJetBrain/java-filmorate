package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {

    private final List<User> users = new ArrayList<>();
    private static int startId = 1;

    @GetMapping
    public List<User> getUsers() {
        log.info("Получен запрос на получение списка пользователей");
        return users;
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        log.info("Получен запрос на создание нового пользователя");
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        user.setId(startId++);
        users.add(user);
        return user;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) throws ValidationException {
        User update = users.stream()
                .filter(u -> u.getId() == user.getId())
                .findAny()
                .orElse(null);
        if (update != null) {
            users.remove(update);
            users.add(user);
            return user;
        } else {
            log.warn("Пользователя с таким id нет - обновление не возможно");
            throw new ValidationException("Нет пользователя с таким id");
        }
    }



}
