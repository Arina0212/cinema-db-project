package ru.urfu.cinema.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.urfu.cinema.domain.Film;
import ru.urfu.cinema.domain.Session;
import ru.urfu.cinema.repository.FilmRepository;
import ru.urfu.cinema.repository.SessionRepository;
import ru.urfu.cinema.web.dto.SessionDto;
import ru.urfu.cinema.web.dto.SessionRequest;
import ru.urfu.cinema.web.error.NotFoundException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class SessionService {

    private final SessionRepository sessionRepository;
    private final FilmRepository filmRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public SessionDto create(SessionRequest request) {
        Film film = filmRepository.findById(request.filmId())
                .orElseThrow(() -> new NotFoundException("Фильм " + request.filmId() + " не найден"));

        Session session = new Session(film, request.startsAt(), request.hall().trim(), request.price());
        film.addSession(session);
        return SessionDto.from(sessionRepository.save(session));
    }

    @Transactional
    public List<SessionDto> findByFilm(Long filmId) {
        if (!filmRepository.existsById(filmId)) {
            throw new NotFoundException("Фильм " + filmId + " не найден");
        }
        return sessionRepository.findByFilmIdOrderByStartsAtAsc(filmId).stream().map(SessionDto::from).toList();
    }

    @Transactional
    public List<SessionDto> schedule(LocalDateTime from, LocalDateTime to) {
        if (from.isAfter(to)) {
            throw new IllegalArgumentException("Начало периода позже его конца");
        }
        return sessionRepository.findSchedule(from, to).stream().map(SessionDto::from).toList();
    }

    @Transactional
    public List<SessionDto> findByGenre(String genre) {
        return sessionRepository.findByFilmGenreNative(genre.trim()).stream().map(SessionDto::from).toList();
    }

    @Transactional
    public List<SessionDto> search(Long filmId,
                                   String hall,
                                   LocalDateTime from,
                                   LocalDateTime to,
                                   BigDecimal maxPrice) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Session> criteriaQuery = criteriaBuilder.createQuery(Session.class);
        Root<Session> root = criteriaQuery.from(Session.class);
        root.fetch("film", JoinType.INNER);

        List<Predicate> predicates = new ArrayList<>();
        if (filmId != null) {
            predicates.add(criteriaBuilder.equal(root.get("film").get("id"), filmId));
        }
        if (hall != null && !hall.isBlank()) {
            predicates.add(criteriaBuilder.equal(
                    criteriaBuilder.lower(root.get("hall")),
                    hall.trim().toLowerCase(Locale.ROOT)));
        }
        if (from != null) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("startsAt"), from));
        }
        if (to != null) {
            predicates.add(criteriaBuilder.lessThan(root.get("startsAt"), to));
        }
        if (maxPrice != null) {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("price"), maxPrice));
        }

        criteriaQuery.where(predicates.toArray(new Predicate[0]));
        criteriaQuery.orderBy(criteriaBuilder.asc(root.get("startsAt")));

        TypedQuery<Session> query = entityManager.createQuery(criteriaQuery);
        return query.getResultList().stream().map(SessionDto::from).toList();
    }
}
