package ru.urfu.cinema.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.urfu.cinema.domain.Film;
import ru.urfu.cinema.repository.FilmRepository;
import ru.urfu.cinema.web.dto.FilmDto;
import ru.urfu.cinema.web.dto.FilmRequest;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FilmService {

    private final FilmRepository filmRepository;

    @Transactional
    public FilmDto create(FilmRequest request) {
        Film film = new Film(request.title().trim(), request.genre().trim(), request.durationMinutes());
        return FilmDto.from(filmRepository.save(film));
    }

    @Transactional
    public List<FilmDto> findAll() {
        return filmRepository.findAll().stream().map(FilmDto::from).toList();
    }

    @Transactional
    public List<FilmDto> findByGenre(String genre) {
        return filmRepository.findByGenreIgnoreCase(genre.trim()).stream().map(FilmDto::from).toList();
    }

    @Transactional
    public List<FilmDto> findByDuration(int minMinutes, int maxMinutes) {
        if (minMinutes > maxMinutes) {
            throw new IllegalArgumentException("Нижняя граница длительности больше верхней");
        }
        return filmRepository.findByDurationRange(minMinutes, maxMinutes).stream().map(FilmDto::from).toList();
    }

    @Transactional
    public List<FilmDto> searchByTitle(String text) {
        return filmRepository.searchByTitleNative(text.trim()).stream().map(FilmDto::from).toList();
    }
}
