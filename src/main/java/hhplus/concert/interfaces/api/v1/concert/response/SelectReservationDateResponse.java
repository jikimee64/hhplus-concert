package hhplus.concert.interfaces.api.v1.concert.response;

import java.time.LocalDate;
import java.util.List;

public record SelectReservationDateResponse(
        List<LocalDate> date
) {
}
