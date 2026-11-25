package ru.urfu.cinema.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.urfu.cinema.domain.BonusCard;
import ru.urfu.cinema.domain.Visitor;
import ru.urfu.cinema.repository.BonusCardRepository;
import ru.urfu.cinema.repository.VisitorRepository;
import ru.urfu.cinema.web.dto.VisitorDto;
import ru.urfu.cinema.web.dto.VisitorRequest;
import ru.urfu.cinema.web.error.ConflictException;
import ru.urfu.cinema.web.error.NotFoundException;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class VisitorService {

    private final VisitorRepository visitorRepository;
    private final BonusCardRepository bonusCardRepository;

    @Transactional
    public VisitorDto register(VisitorRequest request) {
        String email = request.email().trim().toLowerCase(Locale.ROOT);
        visitorRepository.findByEmailIgnoreCase(email).ifPresent(existing -> {
            throw new ConflictException("Посетитель с email " + email + " уже зарегистрирован");
        });

        Visitor savedVisitor = visitorRepository.save(new Visitor(request.fullName().trim(), email));

        BonusCard card = new BonusCard(nextCardNumber(), 0, LocalDate.now());
        card.setVisitor(savedVisitor);
        bonusCardRepository.save(card);
        savedVisitor.attachBonusCard(card);

        return VisitorDto.from(savedVisitor);
    }

    @Transactional
    public VisitorDto findById(Long id) {
        return visitorRepository.findByIdWithBonusCard(id)
                .map(VisitorDto::from)
                .orElseThrow(() -> new NotFoundException("Посетитель " + id + " не найден"));
    }

    @Transactional
    public VisitorDto findByEmail(String email) {
        return visitorRepository.findByEmailIgnoreCase(email.trim())
                .map(VisitorDto::from)
                .orElseThrow(() -> new NotFoundException("Посетитель с email " + email + " не найден"));
    }

    @Transactional
    public List<VisitorDto> findActive(int minTickets) {
        return visitorRepository.findActiveVisitorsNative(minTickets).stream().map(VisitorDto::from).toList();
    }

    private String nextCardNumber() {
        long nextNumber = bonusCardRepository.count() + 1;
        return "BC-%d-%04d".formatted(LocalDate.now().getYear(), nextNumber);
    }
}
