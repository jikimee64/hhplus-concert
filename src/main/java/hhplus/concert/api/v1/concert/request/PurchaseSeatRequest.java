package hhplus.concert.api.v1.concert.request;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

public record PurchaseSeatRequest(
        @Schema(description = "콘서트 오픈 날짜")
        LocalDate concertOpenDate,
        @Schema(description = "구매 금액")
        Integer purchaseAmount
) {
}
