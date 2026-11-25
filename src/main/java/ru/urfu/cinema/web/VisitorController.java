package ru.urfu.cinema.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.urfu.cinema.service.VisitorService;
import ru.urfu.cinema.web.dto.VisitorDto;
import ru.urfu.cinema.web.dto.VisitorRequest;

import java.util.List;

@RestController
@RequestMapping("/api/visitors")
@RequiredArgsConstructor
public class VisitorController {

    private final VisitorService visitorService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public VisitorDto register(@Valid @RequestBody VisitorRequest request) {
        return visitorService.register(request);
    }

    @GetMapping("/{id}")
    public VisitorDto byId(@PathVariable Long id) {
        return visitorService.findById(id);
    }

    @GetMapping
    public VisitorDto byEmail(@RequestParam String email) {
        return visitorService.findByEmail(email);
    }

    @GetMapping("/active")
    public List<VisitorDto> active(@RequestParam(defaultValue = "2") int minTickets) {
        return visitorService.findActive(minTickets);
    }
}
