package ru.urfu.cinema.web.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record TicketRequest(
        @NotNull(message = "Нужен идентификатор посетителя")
        Long visitorId,

        @NotNull(message = "Нужен идентификатор сеанса")
        Long sessionId,

        @Min(value = 1, message = "Номер места начинается с 1")
        int seatNumber
) {
}
