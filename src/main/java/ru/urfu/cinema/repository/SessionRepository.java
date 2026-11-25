package ru.urfu.cinema.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.urfu.cinema.domain.Session;

import java.time.LocalDateTime;
import java.util.List;

public interface SessionRepository extends JpaRepository<Session, Long> {

    List<Session> findByFilmIdOrderByStartsAtAsc(Long filmId);

    @Query("""
            SELECT s FROM Session s
            JOIN FETCH s.film
            WHERE s.startsAt >= :from AND s.startsAt < :to
            ORDER BY s.startsAt
            """)
    List<Session> findSchedule(@Param("from") LocalDateTime from,
                               @Param("to") LocalDateTime to);

    @Query(value = """
            SELECT s.* FROM movie_session s
            JOIN film f ON f.id = s.film_id
            WHERE LOWER(f.genre) = LOWER(:genre)
            ORDER BY s.starts_at
            """, nativeQuery = true)
    List<Session> findByFilmGenreNative(@Param("genre") String genre);
}
