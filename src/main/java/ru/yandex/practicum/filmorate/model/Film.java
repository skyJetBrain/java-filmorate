package ru.yandex.practicum.filmorate.model;

import lombok.Setter;
import lombok.Value;
import lombok.experimental.NonFinal;
import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.Set;

@Value
public class Film {
    @NonFinal
    @Setter
    Long id;
    @NotBlank(message = "У фильма должно быть название")
    String name;
    @Size(max = 200, message = "Описание не должно превышать 200 символов")
    String description;
    LocalDate releaseDate;
    @Positive(message = "Продолжительность фильма должна быть больше 0")
    int duration;
    Set<Long> likes;

}
