package ru.urfu.cinema.web.dto;

import ru.urfu.cinema.domain.BonusCard;

import java.time.LocalDate;

public record BonusCardDto(Long id, String cardNumber, int bonusPoints, LocalDate issuedAt) {

    public static BonusCardDto from(BonusCard card) {
        if (card == null) {
            return null;
        }
        return new BonusCardDto(card.getId(), card.getCardNumber(), card.getBonusPoints(), card.getIssuedAt());
    }
}
