package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage{

    private final List<User> users = new ArrayList<>();
    private static long startId = 1;

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
            throw new ValidationException("Нет пользователя с таким id=" + user.getId());
        }
    }

    @Override
    public void deleteUser(User user) {
        log.info("Получен запрос на удаление пользователя");
        User delete = users.stream()
                .filter(u -> u.getId() == user.getId())
                .findAny()
                .orElseThrow(() -> {
                    log.warn("Такого пользователя нет в списке - удаление не возможно");
                    throw new NotFoundException("Такого пользователя нет в списке");
                });
        users.remove(delete);
    }

    @Override
    public List<User> getAllUsers() {
        log.info("Получен запрос на получение списка пользователей");
        return users;
    }

    @Override
    public User getUser(long id) {
        log.info("Получен запрос на получение фильма по его Id");
        return users.stream()
                .filter(u -> id == u.getId())
                .findFirst().orElseThrow(() -> {
                    log.warn("Пользователя с таким id={} нет - получение не возможно", id);
                    throw new NotFoundException("Нет пользователя с таким id=" + id);
                });
    }

    @Override
    public User addFriend(long id, long friendId) {
        log.info("Получен запрос на добавление в друзья");
        User user = checkUserById(id);
        User friend = checkUserById(friendId);

        if (user.equals(friend)) {
            throw new ValidationException("Невозможно добавить себя к себе в друзья");
        }

        user.addFriend(friendId);
        friend.addFriend(id);
        return user;
    }

    @Override
    public User removeFriend(long id, long friendId) {
        log.info("Получен запрос на удаление из друзей");
        User user = checkUserById(id);
        User friend = checkUserById(friendId);

        if (user.equals(friend)) {
            throw new ValidationException("Невозможно удалить себя из своих друзей");
        }

        user.removeFriend(friendId);
        friend.removeFriend(id);
        return user;
    }

    @Override
    public List<User> getFriends(long id) {
        log.info("Получен запрос на получение списка друзей");
        Set<Long> friends = checkUserById(id).getFriends();
        return users.stream()
                .filter(u -> friends.contains(u.getId()))
                .collect(Collectors.toList());
    }

    @Override
    public List<User> getCommonFriends(long id, long otherId) {
        log.info("Получен запрос на получение списка общих друзей");

        Set<Long> userFriends = checkUserById(id).getFriends();
        Set<Long> temp = new HashSet<>(userFriends);
        Set<Long> otherUserFriends = checkUserById(otherId).getFriends();
        temp.retainAll(otherUserFriends);
        return users.stream()
                .filter(u -> temp.contains(u.getId()))
                .collect(Collectors.toList());

    }

    public User checkUserById(long id) {
        return users.stream()
                .filter(u -> id == u.getId())
                .findFirst()
                .orElseThrow(() -> {
                    log.warn("Пользователя с таким id={} нет в списке пользователей", id);
                    throw new NotFoundException("Нет пользователя с таким id=" + id);
                });
    }
}
