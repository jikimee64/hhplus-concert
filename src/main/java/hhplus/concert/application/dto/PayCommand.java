package hhplus.concert.application.dto;

import java.time.LocalDate;

public record PayCommand(
        Long concertScheduleId,
        Long seatId,
        Long userId,
        LocalDate concertOpenDate
) {
}
