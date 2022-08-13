package ru.yandex.practicum.filmorate.service;

import lombok.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

@Service
@Value
public class UserService {

    private UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User add(User user) {
        return userStorage.add(user);
    }

    public User update(User user) {
        return userStorage.update(user);
    }

    public List<User> getUsers() {
        return userStorage.getAll();
    }

    public void delete(User user) {
        userStorage.delete(user);
    }

    public User getUser(long id) {
        return userStorage.get(id);
    }


    public User addFriend(long id, long friendId) {
        return userStorage.addFriend(id, friendId);
    }

    public User removeFriend(long id, long friendId) {
        return userStorage.removeFriend(id, friendId);
    }

    public List<User> getFriends(long id) {
        return userStorage.getFriends(id);
    }

    public List<User> getCommonFriends(long id, long otherId) {
        return userStorage.getCommonFriends(id, otherId);
    }
}
