package hhplus.concert.api.v1.concert.request;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

public record ReserveSeatRequest(
        @Schema(description = "콘서트 오픈 날짜(2024-01-01)")
        LocalDate concertOpenDate
) {
}
