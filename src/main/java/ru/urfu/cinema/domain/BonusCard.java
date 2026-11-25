package ru.urfu.cinema.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(
        name = "bonus_card",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_bonus_card_number", columnNames = "card_number"),
                @UniqueConstraint(name = "uk_bonus_card_visitor", columnNames = "visitor_id")
        }
)
@Getter
@Setter
@NoArgsConstructor
public class BonusCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "card_number", nullable = false, length = 30)
    private String cardNumber;

    @Column(name = "bonus_points", nullable = false)
    private int bonusPoints;

    @Column(name = "issued_at", nullable = false)
    private LocalDate issuedAt;

    @OneToOne(optional = false)
    @JoinColumn(name = "visitor_id", nullable = false, foreignKey = @ForeignKey(name = "fk_bonus_card_visitor"))
    private Visitor visitor;

    public BonusCard(String cardNumber, int bonusPoints, LocalDate issuedAt) {
        this.cardNumber = cardNumber;
        this.bonusPoints = bonusPoints;
        this.issuedAt = issuedAt;
    }
}
