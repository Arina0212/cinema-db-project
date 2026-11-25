package ru.urfu.cinema.web.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record FilmRequest(
        @NotBlank(message = "Название фильма обязательно")
        @Size(max = 200)
        String title,

        @NotBlank(message = "Жанр обязателен")
        @Size(max = 80)
        String genre,

        @Min(value = 1, message = "Длительность должна быть положительной")
        int durationMinutes
) {
}
