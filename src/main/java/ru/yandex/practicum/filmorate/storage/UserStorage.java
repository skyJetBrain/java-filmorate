package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Set;

public interface UserStorage {

    User createUser(User user);

    User updateUser(User user);

    void deleteUser(User user);

    List<User> getAllUsers();

    User getUser(long id);

    User addFriend(long id, long friendId);

    User removeFriend(long id, long friendId);

    List<User> getFriends(long id);
}
