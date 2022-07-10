package ru.yandex.practicum.filmorate.controllers;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    private final Map<Integer, User> users = new LinkedHashMap<>();

    @GetMapping
    public Map<Integer, User> getUsers() {
        return users;
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        users.put(user.getId(), user);
        return user;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        users.put(user.getId(), user);
        return user;
    }



}
