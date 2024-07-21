package hhplus.concert.application.pay.dto;

import hhplus.concert.domain.pay.dto.Receipt;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record ReceiptResult(
        Long purchaseNumber,
        String concertName,
        LocalDate concertOpenDate,
        Integer seatPosition,
        Integer purchaseAmount,
        LocalDateTime purchaseDate
) {

    public static ReceiptResult from(Receipt receipt) {
        return new ReceiptResult(
                receipt.purchaseNumber(),
                receipt.concertName(),
                receipt.concertOpenDate(),
                receipt.seatPosition(),
                receipt.purchaseAmount(),
                receipt.purchaseDate()
        );
    }
}
