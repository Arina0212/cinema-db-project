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
import ru.urfu.cinema.service.TicketService;
import ru.urfu.cinema.web.dto.SoldTicketsDto;
import ru.urfu.cinema.web.dto.TicketDto;
import ru.urfu.cinema.web.dto.TicketRequest;

import java.util.List;

@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TicketDto buy(@Valid @RequestBody TicketRequest request) {
        return ticketService.buy(request);
    }

    @GetMapping
    public List<TicketDto> byVisitor(@RequestParam Long visitorId) {
        return ticketService.findByVisitor(visitorId);
    }


    @GetMapping("/sold")
    public SoldTicketsDto sold(@RequestParam Long sessionId) {
        return new SoldTicketsDto(sessionId, ticketService.countSold(sessionId));
    }
}
