package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

@Data
@Slf4j
public class User {
    private final int id;
    @NotBlank(message = "Электронная почта не может быть пустой")
    @Email(message = "Адрес электронной почты не содержит символ @")
    private final String email;
    @NotBlank(message = "Логин должен содержать хотя бы один символ")
    @Pattern(regexp = "\\S+", message = "В логине не должно быть пробелов")
    private final String login;
    private String name;
    @PastOrPresent(message = "Дата рождения не может быть в будущем")
    private final LocalDate birthday;

    public String getName() {
        if (this.name == null) {
            log.warn("Имя не введено");
            this.name = this.login;
            return this.name;
        }
        return name;
    }
}
