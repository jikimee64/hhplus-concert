package hhplus.concert.api.v1.concert.response;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record PurchaseSeatReceiptResponse(
        ReceiptResponse receipt
) {

    public record ReceiptResponse(
            Long purchaseNumber,
            String concertName,
            LocalDate concertOpenDate,
            Integer seatPosition,
            Integer purchaseAmount,
            LocalDateTime purchaseDate
    ) {

    }
}
