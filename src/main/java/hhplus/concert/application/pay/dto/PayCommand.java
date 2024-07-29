package hhplus.concert.application.pay.dto;

import java.time.LocalDate;

public record PayCommand(
        Long concertScheduleId,
        Long seatId,
        Long userId,
        LocalDate concertOpenDate
) {
}
