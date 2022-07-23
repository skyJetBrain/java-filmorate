package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserStorage userStorage;

    @Autowired
    public UserController(UserStorage userStorage) {
        this.userStorage = new InMemoryUserStorage();
    }

    @GetMapping
    public List<User> getUsers() {
        return userStorage.getUsers();
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        return userStorage.createUser(user);
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) throws ValidationException {
        return userStorage.updateUser(user);
    }



}
