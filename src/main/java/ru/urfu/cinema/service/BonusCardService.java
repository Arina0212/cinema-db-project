package ru.urfu.cinema.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.urfu.cinema.repository.BonusCardRepository;
import ru.urfu.cinema.web.dto.BonusCardDto;
import ru.urfu.cinema.web.error.NotFoundException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BonusCardService {

    private final BonusCardRepository bonusCardRepository;

    @Transactional
    public BonusCardDto findByNumber(String cardNumber) {
        return bonusCardRepository.findByCardNumber(cardNumber.trim())
                .map(BonusCardDto::from)
                .orElseThrow(() -> new NotFoundException("Карта " + cardNumber + " не найдена"));
    }

    @Transactional
    public List<BonusCardDto> findByMinPoints(int minPoints) {
        return bonusCardRepository.findByMinPoints(minPoints).stream().map(BonusCardDto::from).toList();
    }

    @Transactional
    public long totalPoints() {
        return bonusCardRepository.sumAllPointsNative();
    }
}
