package ru.urfu.cinema.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.urfu.cinema.domain.Ticket;

import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

    List<Ticket> findByVisitorId(Long visitorId);

    boolean existsBySessionIdAndSeatNumber(Long sessionId, int seatNumber);

    @Query("""
            SELECT t FROM Ticket t
            JOIN FETCH t.session s
            JOIN FETCH s.film
            WHERE t.visitor.id = :visitorId
            ORDER BY t.purchasedAt DESC
            """)
    List<Ticket> findWithSessionAndFilmByVisitorId(@Param("visitorId") Long visitorId);

    @Query(value = "SELECT COUNT(*) FROM ticket WHERE session_id = :sessionId", nativeQuery = true)
    long countSoldBySessionNative(@Param("sessionId") Long sessionId);
}
