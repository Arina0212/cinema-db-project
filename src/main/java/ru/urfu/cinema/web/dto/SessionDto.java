package ru.urfu.cinema.web.dto;

import ru.urfu.cinema.domain.Session;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record SessionDto(
        Long id,
        Long filmId,
        String filmTitle,
        LocalDateTime startsAt,
        String hall,
        BigDecimal price
) {
    public static SessionDto from(Session session) {
        return new SessionDto(
                session.getId(),
                session.getFilm().getId(),
                session.getFilm().getTitle(),
                session.getStartsAt(),
                session.getHall(),
                session.getPrice()
        );
    }
}
