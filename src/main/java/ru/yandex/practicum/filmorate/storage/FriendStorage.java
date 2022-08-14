package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface FriendStorage {
    void addFriend(long fromUserId, long toUserId);

    List<User> getAllFriends(long id);

    List<User> getCommonFriends(long id, long otherId);

    void deleteFromFriends(long id, long otherId);
}
