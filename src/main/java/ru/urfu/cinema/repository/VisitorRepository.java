package ru.urfu.cinema.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.urfu.cinema.domain.Visitor;

import java.util.List;
import java.util.Optional;

public interface VisitorRepository extends JpaRepository<Visitor, Long> {

    Optional<Visitor> findByEmailIgnoreCase(String email);

    @Query("""
            SELECT v FROM Visitor v
            LEFT JOIN FETCH v.bonusCard
            WHERE v.id = :id
            """)
    Optional<Visitor> findByIdWithBonusCard(@Param("id") Long id);

    @Query(value = """
            SELECT v.* FROM visitor v
            WHERE v.id IN (
                SELECT t.visitor_id FROM ticket t
                GROUP BY t.visitor_id
                HAVING COUNT(*) >= :minTickets
            )
            ORDER BY v.full_name
            """, nativeQuery = true)
    List<Visitor> findActiveVisitorsNative(@Param("minTickets") int minTickets);
}
