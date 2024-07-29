package hhplus.concert.application.concert.dto;

import java.time.LocalDate;

public record ReservationSeatCommand(
        Long concertScheduleId,
        Long userId,
        Integer seatPosition,
        Integer seatAmount,
        LocalDate concertOpenDate
) {
}
