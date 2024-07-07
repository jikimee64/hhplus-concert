package hhplus.concert.api.v1.concert.request;

import java.time.LocalDate;

public record ReserveSeatRequest(
        LocalDate concertOpenDate
) {
}
