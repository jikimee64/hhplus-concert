package hhplus.concert.application.dto;

import java.time.LocalDate;

public record ReservationSeatCommand(
        Long concertScheduleId,
        Long userId,
        Integer seatPosition,
        Integer seatAmount,
        LocalDate concertOpenDate
) {
}
