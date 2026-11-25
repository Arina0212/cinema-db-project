package ru.urfu.cinema.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.urfu.cinema.domain.BonusCard;
import ru.urfu.cinema.domain.Session;
import ru.urfu.cinema.domain.Ticket;
import ru.urfu.cinema.domain.Visitor;
import ru.urfu.cinema.repository.SessionRepository;
import ru.urfu.cinema.repository.TicketRepository;
import ru.urfu.cinema.repository.VisitorRepository;
import ru.urfu.cinema.web.dto.TicketDto;
import ru.urfu.cinema.web.dto.TicketRequest;
import ru.urfu.cinema.web.error.ConflictException;
import ru.urfu.cinema.web.error.NotFoundException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TicketService {

    private static final int SEATS_IN_HALL = 40;
    private static final int DISCOUNT_FROM_POINTS = 100;
    private static final BigDecimal DISCOUNT_MULTIPLIER = new BigDecimal("0.90");

    private final TicketRepository ticketRepository;
    private final VisitorRepository visitorRepository;
    private final SessionRepository sessionRepository;

    @Transactional
    public TicketDto buy(TicketRequest request) {
        Visitor visitor = visitorRepository.findById(request.visitorId())
                .orElseThrow(() -> new NotFoundException("Посетитель " + request.visitorId() + " не найден"));
        Session session = sessionRepository.findById(request.sessionId())
                .orElseThrow(() -> new NotFoundException("Сеанс " + request.sessionId() + " не найден"));

        if (request.seatNumber() > SEATS_IN_HALL) {
            throw new IllegalArgumentException("В зале всего " + SEATS_IN_HALL + " мест");
        }
        if (ticketRepository.existsBySessionIdAndSeatNumber(session.getId(), request.seatNumber())) {
            throw new ConflictException("Место " + request.seatNumber() + " на этот сеанс уже продано");
        }

        BonusCard card = visitor.getBonusCard();
        BigDecimal price = session.getPrice();
        if (card != null && card.getBonusPoints() >= DISCOUNT_FROM_POINTS) {
            price = price.multiply(DISCOUNT_MULTIPLIER).setScale(2, RoundingMode.HALF_UP);
        }

        Ticket ticket = new Ticket(visitor, session, request.seatNumber(), price, LocalDateTime.now());
        visitor.addTicket(ticket);
        session.addTicket(ticket);
        Ticket saved = ticketRepository.save(ticket);

        if (card != null) {
            int earned = price.divide(BigDecimal.TEN, 0, RoundingMode.DOWN).intValue();
            card.setBonusPoints(card.getBonusPoints() + earned);
        }

        return TicketDto.from(saved);
    }

    @Transactional
    public List<TicketDto> findByVisitor(Long visitorId) {
        if (!visitorRepository.existsById(visitorId)) {
            throw new NotFoundException("Посетитель " + visitorId + " не найден");
        }
        return ticketRepository.findWithSessionAndFilmByVisitorId(visitorId).stream().map(TicketDto::from).toList();
    }

    @Transactional
    public long countSold(Long sessionId) {
        if (!sessionRepository.existsById(sessionId)) {
            throw new NotFoundException("Сеанс " + sessionId + " не найден");
        }
        return ticketRepository.countSoldBySessionNative(sessionId);
    }
}
