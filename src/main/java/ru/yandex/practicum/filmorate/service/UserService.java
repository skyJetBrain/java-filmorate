package ru.yandex.practicum.filmorate.service;

import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FriendStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

@Service
@Value
@Slf4j
public class UserService {

    UserStorage userStorage;
    FriendStorage friendStorage;

    @Autowired
    public UserService(@Qualifier("UserDbStorage")UserStorage userStorage, FriendStorage friendStorage) {
        this.userStorage = userStorage;
        this.friendStorage = friendStorage;
    }

    public User add(User user) {
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        return userStorage.add(user);
    }

    public User update(User user) {
        if (userStorage.get(user.getId()) != null) {
            log.debug("Обновлены данные пользователя: {}", user);
            userStorage.update(user);
        } else {
            throw new NotFoundException("Пользователь не найден");
        }
        return userStorage.get(user.getId());
    }

    public List<User> getUsers() {
        return userStorage.getAll();
    }

    public void delete(User user) {
        userStorage.delete(user);
    }

    public User getUser(long id) {
        if (userStorage.get(id) == null) {
            throw new NotFoundException("Объект не найден");
        }
        return userStorage.get(id);
    }


    public User addFriend(long id, long friendId) {
        User user = userStorage.get(id);
        User friend = userStorage.get(friendId);
        if (user == null || friend == null) {
            throw new NotFoundException("Пользователь не найден");
        }
        friendStorage.addFriend(id, friendId);

        return userStorage.addFriend(id, friendId);
    }

    public User removeFriend(long id, long friendId) {
        User user = userStorage.get(id);
        User friend = userStorage.get(friendId);
        if (user == null || friend == null) {
            throw new NotFoundException("Пользователь не найден");
        }
        friendStorage.deleteFromFriends(id, friendId);
        return userStorage.removeFriend(id, friendId);
    }

    public List<User> getFriends(long id) {
        User user = userStorage.get(id);
        if (user == null) {
            throw new NotFoundException("Пользователь не найден");
        }
        return friendStorage.getAllFriends(id);
    }

    public List<User> getCommonFriends(long id, long otherId) {
        User user = userStorage.get(id);
        User otherUser = userStorage.get(otherId);
        if (user == null || otherUser == null) {
            throw new NotFoundException("Пользователь не найден");
        }
        return friendStorage.getCommonFriends(id, otherId);
    }
}
