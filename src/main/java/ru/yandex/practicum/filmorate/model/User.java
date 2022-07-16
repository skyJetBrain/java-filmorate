package ru.yandex.practicum.filmorate.model;

import lombok.Setter;
import lombok.Value;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

@Value
public class User {

    @NonFinal
    @Setter
    private Long id;
    @NotBlank(message = "Электронная почта не может быть пустой")
    @Email(message = "Адрес электронной почты не содержит символ @")
    String email;
    @NotBlank(message = "Логин должен содержать хотя бы один символ")
    @Pattern(regexp = "\\S+", message = "В логине не должно быть пробелов")
    String login;
    @NonFinal
    @Setter
    String name;
    @PastOrPresent(message = "Дата рождения не может быть в будущем")
    LocalDate birthday;


}
