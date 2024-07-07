package hhplus.concert.api.v1.concert.request;

import java.time.LocalDate;

public record PurchaseSeatRequest(
    LocalDate concertOpenDate,
    Integer purchaseAmount
) {
}
