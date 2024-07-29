package hhplus.concert.domain.concert;

import hhplus.concert.support.holder.TimeHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Transactional
@RequiredArgsConstructor
public class SeatAssignReleaseChecker {

    private final ConcertRepository concertRepository;
    private final TimeHolder timeHolder;

    public void release() {
        LocalDateTime expiredAt = timeHolder.currentDateTime().minusMinutes(5);
        List<Reservation> reservations = concertRepository.findReservationReleaseTarget(expiredAt);
        concertRepository.deleteReservation(reservations);
        concertRepository.deletePaymentBy(getReservationIds(reservations));
        concertRepository.deleteSeats(getSeatIds(reservations));
    }

    private List<Long> getReservationIds(List<Reservation> reservations) {
        return reservations.stream().map(Reservation::getId).toList();
    }

    private List<Long> getSeatIds(List<Reservation> reservations) {
        return reservations.stream().map(Reservation::getSeatId).toList();
    }

}
