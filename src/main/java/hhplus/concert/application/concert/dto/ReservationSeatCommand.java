package hhplus.concert.application.concert.dto;

public record ReservationSeatCommand(
        Long concertScheduleId,
        Long userId,
        Long seatId
) {
}
