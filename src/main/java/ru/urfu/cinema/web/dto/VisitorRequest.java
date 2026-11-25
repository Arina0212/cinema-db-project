package ru.urfu.cinema.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record VisitorRequest(
        @NotBlank(message = "ФИО обязательно")
        @Size(max = 150)
        String fullName,

        @NotBlank(message = "Email обязателен")
        @Email(message = "Некорректный email")
        @Size(max = 120)
        String email
) {
}
