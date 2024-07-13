package hhplus.concert.interfaces.api.v1.concert.request;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

public record PurchaseSeatRequest(
        @Schema(description = "유저 고유값")
        Long userId,
        @Schema(description = "콘서트 오픈 날짜")
        LocalDate concertOpenDate
) {
}
