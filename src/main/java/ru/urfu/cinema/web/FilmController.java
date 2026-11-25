package ru.urfu.cinema.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.urfu.cinema.service.FilmService;
import ru.urfu.cinema.web.dto.FilmDto;
import ru.urfu.cinema.web.dto.FilmRequest;

import java.util.List;

@RestController
@RequestMapping("/api/films")
@RequiredArgsConstructor
public class FilmController {

    private final FilmService filmService;

    @GetMapping
    public List<FilmDto> list(@RequestParam(required = false) String genre) {
        return genre == null || genre.isBlank()
                ? filmService.findAll()
                : filmService.findByGenre(genre);
    }

    @GetMapping("/by-duration")
    public List<FilmDto> byDuration(@RequestParam int minMinutes, @RequestParam int maxMinutes) {
        return filmService.findByDuration(minMinutes, maxMinutes);
    }

    @GetMapping("/search")
    public List<FilmDto> search(@RequestParam String title) {
        return filmService.searchByTitle(title);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FilmDto create(@Valid @RequestBody FilmRequest request) {
        return filmService.create(request);
    }
}
