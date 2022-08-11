package ru.yandex.practicum.filmorate.model;

import lombok.Value;

@Value
public class Friendship {
    long fromUserId;
    long toUserId;
    boolean isConfirmed;
}
