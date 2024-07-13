package hhplus.concert.domain;

import hhplus.concert.support.holder.TimeHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class SeatAssignReleaseChecker {

    private final ConcertRepository concertRepository;
    private final TimeHolder timeHolder;

    @Transactional
    public void release() {
        LocalDateTime expiredAt = timeHolder.currentDateTime().minusMinutes(5);
        List<Reservation> reservations = concertRepository.findReservationReleaseTarget(expiredAt);
        concertRepository.deleteReservation(reservations);
        concertRepository.deleteSeats(reservations.stream().map(Reservation::getSeatId).toList());
    }

}
