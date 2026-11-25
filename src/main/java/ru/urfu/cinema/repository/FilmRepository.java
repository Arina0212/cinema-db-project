package ru.urfu.cinema.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.urfu.cinema.domain.Film;

import java.util.List;

public interface FilmRepository extends JpaRepository<Film, Long> {

    List<Film> findByGenreIgnoreCase(String genre);

    @Query("""
            SELECT f FROM Film f
            WHERE f.durationMinutes BETWEEN :minMinutes AND :maxMinutes
            ORDER BY f.durationMinutes
            """)
    List<Film> findByDurationRange(@Param("minMinutes") int minMinutes,
                                   @Param("maxMinutes") int maxMinutes);

    @Query(value = """
            SELECT * FROM film
            WHERE LOWER(title) LIKE LOWER(CONCAT('%', :text, '%'))
            ORDER BY title
            """, nativeQuery = true)
    List<Film> searchByTitleNative(@Param("text") String text);
}
