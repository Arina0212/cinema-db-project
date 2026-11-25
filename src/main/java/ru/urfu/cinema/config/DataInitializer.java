package ru.urfu.cinema.config;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ru.urfu.cinema.domain.BonusCard;
import ru.urfu.cinema.domain.Film;
import ru.urfu.cinema.domain.Session;
import ru.urfu.cinema.domain.Visitor;
import ru.urfu.cinema.repository.BonusCardRepository;
import ru.urfu.cinema.repository.FilmRepository;
import ru.urfu.cinema.repository.SessionRepository;
import ru.urfu.cinema.repository.TicketRepository;
import ru.urfu.cinema.repository.VisitorRepository;
import ru.urfu.cinema.service.TicketService;
import ru.urfu.cinema.web.dto.TicketRequest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private final FilmRepository filmRepository;
    private final SessionRepository sessionRepository;
    private final VisitorRepository visitorRepository;
    private final BonusCardRepository bonusCardRepository;
    private final TicketRepository ticketRepository;
    private final TicketService ticketService;

    @Override
    public void run(String... args) {
        if (filmRepository.count() > 0) {
            log.info("В базе уже есть данные — демонстрационные записи не добавляются");
            return;
        }

        Film interstellar = filmRepository.save(new Film("Интерстеллар", "фантастика", 169));
        Film dune = filmRepository.save(new Film("Дюна: Часть вторая", "фантастика", 166));
        Film oppenheimer = filmRepository.save(new Film("Оппенгеймер", "биография", 180));
        Film barbie = filmRepository.save(new Film("Барби", "комедия", 114));

        LocalDate tomorrow = LocalDate.now().plusDays(1);
        Session eveningInterstellar = sessionRepository.save(
                new Session(interstellar, tomorrow.atTime(18, 0), "Зал 1", new BigDecimal("450.00")));
        Session lateInterstellar = sessionRepository.save(
                new Session(interstellar, tomorrow.atTime(21, 30), "Зал 2", new BigDecimal("380.00")));
        Session eveningDune = sessionRepository.save(
                new Session(dune, tomorrow.atTime(19, 15), "Зал 3", new BigDecimal("520.00")));
        Session matineeOppenheimer = sessionRepository.save(
                new Session(oppenheimer, tomorrow.plusDays(1).atTime(14, 0), "Зал 1", new BigDecimal("400.00")));
        Session eveningBarbie = sessionRepository.save(
                new Session(barbie, tomorrow.plusDays(1).atTime(20, 0), "Зал 2", new BigDecimal("350.00")));

        Visitor arina = createVisitor("Арина Шубина", "arina.shubina@example.com", 120);
        Visitor ivan = createVisitor("Иван Петров", "ivan.petrov@example.com", 40);
        Visitor maria = createVisitor("Мария Соколова", "maria.sokolova@example.com", 0);

        ticketService.buy(new TicketRequest(arina.getId(), eveningInterstellar.getId(), 5));
        ticketService.buy(new TicketRequest(ivan.getId(), eveningInterstellar.getId(), 6));
        ticketService.buy(new TicketRequest(maria.getId(), eveningDune.getId(), 12));
        ticketService.buy(new TicketRequest(arina.getId(), matineeOppenheimer.getId(), 3));
        ticketService.buy(new TicketRequest(ivan.getId(), eveningBarbie.getId(), 8));

        log.info("""
                Демо-данные загружены: фильмов {}, сеансов {}, посетителей {}, билетов {}.
                Ближайший сеанс: «{}» в {}, {}.
                """,
                filmRepository.count(),
                sessionRepository.count(),
                visitorRepository.count(),
                ticketRepository.count(),
                eveningInterstellar.getFilm().getTitle(),
                eveningInterstellar.getHall(),
                eveningInterstellar.getStartsAt());
    }

    private Visitor createVisitor(String fullName, String email, int bonusPoints) {
        Visitor visitor = visitorRepository.save(new Visitor(fullName, email));

        String cardNumber = "BC-%d-%04d".formatted(LocalDate.now().getYear(), bonusCardRepository.count() + 1);
        BonusCard card = new BonusCard(cardNumber, bonusPoints, LocalDate.now().minusMonths(3));
        card.setVisitor(visitor);
        bonusCardRepository.save(card);
        visitor.attachBonusCard(card);

        return visitor;
    }
}
