package hhplus.concert.interfaces.api.v1.concert.request;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

public record ReserveSeatRequest(
        @Schema(description = "유저 고유값")
        Long userId,
        @Schema(description = "좌석 번호")
        Integer seatPosition,
        @Schema(description = "좌석 가격")
        Integer seatAmount,
        @Schema(description = "콘서트 오픈 날짜(2024-01-01)")
        LocalDate concertOpenDate
) {
}
