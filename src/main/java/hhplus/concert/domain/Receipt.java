package hhplus.concert.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record Receipt(
        Long purchaseNumber,
        String concertName,
        LocalDate concertOpenDate,
        Integer seatPosition,
        Integer purchaseAmount,
        LocalDateTime purchaseDate
) {
}
