package hhplus.concert.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ConcertRepository {
    List<ConcertSchedule> findConcertScheduleBy(Long concertId, TotalSeatStatus status);
    Optional<Reservation> findReservation(Long concertScheduleId, Long seatId);
    List<SeatQueryDto> findConcertSeat(Long concertScheduleId);
    ConcertSeat saveSeat(ConcertSeat concertSeat);
    Optional<ConcertSeat> findSeatBy(Long concertScheduleId, Integer position);
    ConcertSchedule findConcertSchedule(Long concertScheduleId);
    Integer updateReservationStatus(ReservationStatus status, Long concertScheduleId, Long seatId);
    Reservation saveReservation(Reservation reservation);
    List<Reservation> findReservationReleaseTarget(LocalDateTime expiredAt);
    void deleteReservation(List<Reservation> reservations);
    void deleteSeats(List<Long> seats);
    List<Reservation> findBy(Long concertScheduleId);
}
