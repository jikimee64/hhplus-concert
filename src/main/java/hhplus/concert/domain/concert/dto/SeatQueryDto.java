package hhplus.concert.domain.concert.dto;

import com.querydsl.core.annotations.QueryProjection;
import hhplus.concert.domain.concert.ReservationStatus;

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
