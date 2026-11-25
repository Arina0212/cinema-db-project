package ru.urfu.cinema.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "ticket",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_ticket_session_seat",
                columnNames = {"session_id", "seat_number"}
        )
)
@Getter
@Setter
@NoArgsConstructor
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "visitor_id", nullable = false, foreignKey = @ForeignKey(name = "fk_ticket_visitor"))
    private Visitor visitor;

    @ManyToOne(optional = false)
    @JoinColumn(name = "session_id", nullable = false, foreignKey = @ForeignKey(name = "fk_ticket_session"))
    private Session session;

    @Column(name = "seat_number", nullable = false)
    private int seatNumber;

    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "purchased_at", nullable = false)
    private LocalDateTime purchasedAt;

    public Ticket(Visitor visitor, Session session, int seatNumber, BigDecimal price, LocalDateTime purchasedAt) {
        this.visitor = visitor;
        this.session = session;
        this.seatNumber = seatNumber;
        this.price = price;
        this.purchasedAt = purchasedAt;
    }
}
