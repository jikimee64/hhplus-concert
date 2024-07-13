package hhplus.concert.interfaces.api.v1.concert.response;

import java.util.List;

public record SelectReservationSeatResponse(
        List<SeatResponse> seats
) {
    public record SeatResponse(
            Long id,
            Integer position,
            Integer price,
            String status
    ) {
    }

}



