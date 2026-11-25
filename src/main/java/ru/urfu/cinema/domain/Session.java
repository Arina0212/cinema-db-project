package ru.urfu.cinema.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "movie_session",
        indexes = @Index(name = "ix_session_film", columnList = "film_id")
)
@Getter
@Setter
@NoArgsConstructor
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(
            name = "film_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_session_film")
    )
    private Film film;

    @Column(name = "starts_at", nullable = false)
    private LocalDateTime startsAt;

    @Column(name = "hall", nullable = false, length = 40)
    private String hall;

    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @OneToMany(mappedBy = "session")
    private List<Ticket> tickets = new ArrayList<>();

    public Session(Film film, LocalDateTime startsAt, String hall, BigDecimal price) {
        this.film = film;
        this.startsAt = startsAt;
        this.hall = hall;
        this.price = price;
    }

    public void addTicket(Ticket ticket) {
        tickets.add(ticket);
        ticket.setSession(this);
    }
}
