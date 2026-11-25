package ru.urfu.cinema.web.dto;

import ru.urfu.cinema.domain.Visitor;

public record VisitorDto(Long id, String fullName, String email, BonusCardDto bonusCard) {

    public static VisitorDto from(Visitor visitor) {
        return new VisitorDto(
                visitor.getId(),
                visitor.getFullName(),
                visitor.getEmail(),
                BonusCardDto.from(visitor.getBonusCard())
        );
    }
}
