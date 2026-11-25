package ru.urfu.cinema.web.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record SessionRequest(
        @NotNull(message = "Нужен идентификатор фильма")
        Long filmId,

        @NotNull(message = "Нужно время начала сеанса")
        LocalDateTime startsAt,

        @NotBlank(message = "Нужен зал")
        @Size(max = 40)
        String hall,

        @NotNull(message = "Нужна цена билета")
        @DecimalMin(value = "0.0", inclusive = false, message = "Цена должна быть больше нуля")
        BigDecimal price
) {
}
