package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.Setter;
import lombok.Value;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Value
@Slf4j
public class Film {
    @NonFinal
    @Setter
    int id;
    @NotBlank(message = "У фильма должно быть название")
    String name;
    @Size(max = 200, message = "Описание не должно превышать 200 символов")
    String description;
    LocalDate releaseDate;
    @Positive(message = "Продолжительность фильма должна быть больше 0")
    int duration;

}
