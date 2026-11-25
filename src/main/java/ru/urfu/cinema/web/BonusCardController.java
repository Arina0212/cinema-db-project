package ru.urfu.cinema.web;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.urfu.cinema.service.BonusCardService;
import ru.urfu.cinema.web.dto.BonusCardDto;
import ru.urfu.cinema.web.dto.TotalPointsDto;

import java.util.List;

@RestController
@RequestMapping("/api/bonus-cards")
@RequiredArgsConstructor
public class BonusCardController {

    private final BonusCardService bonusCardService;

    @GetMapping("/{cardNumber}")
    public BonusCardDto byNumber(@PathVariable String cardNumber) {
        return bonusCardService.findByNumber(cardNumber);
    }

    @GetMapping
    public List<BonusCardDto> byMinPoints(@RequestParam(defaultValue = "0") int minPoints) {
        return bonusCardService.findByMinPoints(minPoints);
    }

    @GetMapping("/total-points")
    public TotalPointsDto totalPoints() {
        return new TotalPointsDto(bonusCardService.totalPoints());
    }
}
