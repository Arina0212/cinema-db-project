package ru.urfu.cinema.web.dto;

import ru.urfu.cinema.domain.Film;

public record FilmDto(Long id, String title, String genre, int durationMinutes) {

    public static FilmDto from(Film film) {
        return new FilmDto(film.getId(), film.getTitle(), film.getGenre(), film.getDurationMinutes());
    }
}
