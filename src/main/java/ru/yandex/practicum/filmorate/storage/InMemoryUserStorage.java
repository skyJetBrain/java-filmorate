package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage{

    private final List<User> users = new ArrayList<>();
    private static Long startId = 1L;

    @Override
    public User createUser(User user) {
        log.info("Получен запрос на создание нового пользователя");
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        user.setId(startId++);
        users.add(user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        User update = users.stream()
                .filter(u -> u.getId() == user.getId())
                .findAny()
                .orElse(null);
        if (update != null) {
            users.remove(update);
            users.add(user);
            return user;
        } else {
            log.warn("Пользователя с таким id={} нет - обновление не возможно", user.getId());
            throw new ValidationException("Нет пользователя с таким id");
        }
    }

    @Override
    public void deleteUser(User user) {
        User delete = users.stream()
                .filter(u -> u.getId() == user.getId())
                .findAny()
                .orElse(null);
        users.remove(delete);
    }

    @Override
    public List<User> getAllUsers() {
        log.info("Получен запрос на получение списка пользователей");
        return users;
    }
}
