package ru.urfu.cinema.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.urfu.cinema.domain.BonusCard;

import java.util.List;
import java.util.Optional;

public interface BonusCardRepository extends JpaRepository<BonusCard, Long> {

    Optional<BonusCard> findByCardNumber(String cardNumber);

    @Query("""
            SELECT b FROM BonusCard b
            WHERE b.bonusPoints >= :minPoints
            ORDER BY b.bonusPoints DESC
            """)
    List<BonusCard> findByMinPoints(@Param("minPoints") int minPoints);

    @Query(value = "SELECT COALESCE(SUM(bonus_points), 0) FROM bonus_card", nativeQuery = true)
    long sumAllPointsNative();
}
