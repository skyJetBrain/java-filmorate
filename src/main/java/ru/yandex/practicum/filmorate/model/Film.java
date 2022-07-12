package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exception.ValidationException;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
@Slf4j
public class Film {
    private final int id;
    @NotBlank(message = "У фильма должно быть название")
    private final String name;
    @Size(max = 200, message = "Описание не должно превышать 200 символов")
    private final String description;
    private final LocalDate releaseDate;
    @Positive(message = "Продолжительность фильма должна быть больше 0")
    private final int duration;

}
