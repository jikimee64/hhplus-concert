package hhplus.concert.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ConcertRepository {
    List<ConcertSchedule> findConcertScheduleBy(Long concertId, TotalSeatStatus status);
    Optional<Reservation> findReservation(Long concertScheduleId, Long seatId);
    List<SeatQueryDto> findConcertSeat(Long concertScheduleId);
    ConcertSeat findSeat(Long seatId);
    ConcertSchedule findConcertSchedule(Long concertScheduleId);
    Reservation saveReservation(Reservation reservation);
    List<Reservation> findReservationReleaseTarget(LocalDateTime expiredAt);
    void deleteReservation(List<Reservation> reservations);
}
