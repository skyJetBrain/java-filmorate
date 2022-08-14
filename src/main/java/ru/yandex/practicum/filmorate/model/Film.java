package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Setter;
import lombok.Value;
import lombok.experimental.NonFinal;
import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Value
@Builder
public class Film {
    @NonFinal
    @Setter
    long id;
    @NotBlank(message = "У фильма должно быть название")
    String name;
    @Size(max = 200, message = "Описание не должно превышать 200 символов")
    String description;
    LocalDate releaseDate;
    @Positive(message = "Продолжительность фильма должна быть больше 0")
    int duration;
    Set<Long> likes = new HashSet<>();
    Set<Genre> genres = new HashSet<>();
    Mpa mpa;

    public void addLike (long id){
        likes.add(id);
    }

    public void removeLike (long id){
        likes.remove(id);
    }

    public int getLikesCount(){
        return likes.size();
    }

}
