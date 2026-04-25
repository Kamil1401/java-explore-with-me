package ru.practicum.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NewUserRequest {

    @NotBlank(message = "Имя обязательно для заполнения")
    private String name;

    @Email(message = "Некорректный адрес электронной почты")
    @NotBlank(message = "Почта обязательна для заполнения")
    private String email;
}
