package hhplus.concert.domain.concert;

import hhplus.concert.domain.concert.dto.SeatQueryDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ConcertRepository {
    List<ConcertSchedule> findConcertSchedules();
    List<ConcertSchedule> findConcertScheduleBy(Long concertId, TotalSeatStatus status);
    List<ConcertSchedule> findConcertSchedules(List<Long> concertScheduleIds);
    Optional<Reservation> findReservation(Long concertScheduleId, Long seatId);
    Optional<Reservation> findReservationWithLock(Long concertScheduleId, Long seatId);
    List<SeatQueryDto> findConcertSeat(Long concertScheduleId);
    ConcertSeat findSeat(Long seatId);
    ConcertSchedule findConcertSchedule(Long concertScheduleId);
    Integer updateReservationStatus(ReservationStatus status, Long concertScheduleId, Long seatId);
    Reservation saveReservation(Reservation reservation);
    List<Reservation> findReservationReleaseTarget(LocalDateTime expiredAt);
    List<Reservation> findReservations(List<Long> concertScheduleIds);
    void deleteReservation(List<Reservation> reservations);
    void deletePaymentBy(List<Long> reservationIds);
    void deleteSeats(List<Long> seats);
    List<Reservation> findBy(Long concertScheduleId);
    void evictCachedConcertSchedule(Long concertId);
}
