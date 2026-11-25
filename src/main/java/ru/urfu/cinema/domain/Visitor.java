package ru.urfu.cinema.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "visitor",
        uniqueConstraints = @UniqueConstraint(name = "uk_visitor_email", columnNames = "email")
)
@Getter
@Setter
@NoArgsConstructor
public class Visitor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "full_name", nullable = false, length = 150)
    private String fullName;

    @Column(name = "email", nullable = false, length = 120)
    private String email;

    @OneToOne(mappedBy = "visitor")
    private BonusCard bonusCard;

    @OneToMany(mappedBy = "visitor")
    private List<Ticket> tickets = new ArrayList<>();

    public Visitor(String fullName, String email) {
        this.fullName = fullName;
        this.email = email;
    }

    public void attachBonusCard(BonusCard card) {
        this.bonusCard = card;
        card.setVisitor(this);
    }

    public void addTicket(Ticket ticket) {
        tickets.add(ticket);
        ticket.setVisitor(this);
    }
}
