package ru.urfu.cinema.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "film",
        uniqueConstraints = @UniqueConstraint(name = "uk_film_title", columnNames = "title")
)
@Getter
@Setter
@NoArgsConstructor
public class Film {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false, length = 200)
    private String title;

    @Column(name = "genre", nullable = false, length = 80)
    private String genre;

    @Column(name = "duration_minutes", nullable = false)
    private int durationMinutes;

    @OneToMany(mappedBy = "film")
    private List<Session> sessions = new ArrayList<>();

    public Film(String title, String genre, int durationMinutes) {
        this.title = title;
        this.genre = genre;
        this.durationMinutes = durationMinutes;
    }

    public void addSession(Session session) {
        sessions.add(session);
        session.setFilm(this);
    }
}
