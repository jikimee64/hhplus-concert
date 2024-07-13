package hhplus.concert.domain;

import com.querydsl.core.annotations.QueryProjection;

public record SeatQueryDto(
        Long seatId,
        Integer totalSeat,
        Integer position,
        Integer amount,
        ReservationStatus status
) {

    @QueryProjection
    public SeatQueryDto {
    }
}
