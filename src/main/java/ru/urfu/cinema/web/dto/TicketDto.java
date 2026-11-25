package ru.urfu.cinema.web.dto;

import ru.urfu.cinema.domain.Ticket;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TicketDto(
        Long id,
        Long visitorId,
        String visitorName,
        Long sessionId,
        String filmTitle,
        LocalDateTime startsAt,
        String hall,
        int seatNumber,
        BigDecimal price,
        LocalDateTime purchasedAt
) {
    public static TicketDto from(Ticket ticket) {
        var session = ticket.getSession();
        return new TicketDto(
                ticket.getId(),
                ticket.getVisitor().getId(),
                ticket.getVisitor().getFullName(),
                session.getId(),
                session.getFilm().getTitle(),
                session.getStartsAt(),
                session.getHall(),
                ticket.getSeatNumber(),
                ticket.getPrice(),
                ticket.getPurchasedAt()
        );
    }
}
