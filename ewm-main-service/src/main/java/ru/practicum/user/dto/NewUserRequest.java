package ru.practicum.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NewUserRequest {

    @NotBlank(message = "Почта обязательна для заполнения")
    @Email(message = "Некорректный адрес электронной почты")
    @Size(min = 6, max = 254)
    private String email;

    @NotBlank(message = "Имя обязательно для заполнения")
    @Size(min = 2, max = 250)
    private String name;
}
