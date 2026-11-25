package ru.urfu.cinema.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.urfu.cinema.service.SessionService;
import ru.urfu.cinema.web.dto.SessionDto;
import ru.urfu.cinema.web.dto.SessionRequest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/sessions")
@RequiredArgsConstructor
public class SessionController {

    private final SessionService sessionService;

    @GetMapping
    public List<SessionDto> byFilm(@RequestParam Long filmId) {
        return sessionService.findByFilm(filmId);
    }

    @GetMapping("/schedule")
    public List<SessionDto> schedule(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to) {
        return sessionService.schedule(from, to);
    }

    @GetMapping("/by-genre")
    public List<SessionDto> byGenre(@RequestParam String genre) {
        return sessionService.findByGenre(genre);
    }

    @GetMapping("/search")
    public List<SessionDto> search(
            @RequestParam(required = false) Long filmId,
            @RequestParam(required = false) String hall,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to,
            @RequestParam(required = false) BigDecimal maxPrice) {
        return sessionService.search(filmId, hall, from, to, maxPrice);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SessionDto create(@Valid @RequestBody SessionRequest request) {
        return sessionService.create(request);
    }
}
