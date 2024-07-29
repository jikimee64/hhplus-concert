package hhplus.concert.infra.persistence;

import hhplus.concert.domain.concert.*;
import hhplus.concert.domain.concert.dto.SeatQueryDto;
import hhplus.concert.interfaces.api.support.ApiException;
import hhplus.concert.interfaces.api.support.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.logging.LogLevel;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ConcertRepositoryImpl implements ConcertRepository {

    private final ReservationJpaRepository reservationJpaRepository;
    private final PaymentJpaRepository paymentJpaRepository;
    private final ConcertScheduleJpaRepository concertScheduleJpaRepository;
    private final ConcertSeatJpaRepository concertSeatJpaRepository;
    private final ConcertQueryRepository concertQueryRepository;

    @Override
    public List<ConcertSchedule> findConcertScheduleBy(Long concertId, TotalSeatStatus status) {
        return concertScheduleJpaRepository.findByConcertIdAndStatus(concertId, status);
    }

    @Override
    public Optional<Reservation> findReservation(Long concertScheduleId, Long seatId) {
        return reservationJpaRepository.findByConcertScheduleIdAndSeatId(concertScheduleId, seatId);
    }

    @Override
    public List<SeatQueryDto> findConcertSeat(Long concertScheduleId) {
        return concertQueryRepository.findConcertSeat(concertScheduleId);
    }

    @Override
    public ConcertSeat saveSeat(ConcertSeat concertSeat) {
        return concertSeatJpaRepository.save(concertSeat);
    }

    public Optional<ConcertSeat> findSeatBy(Long concertScheduleId, Integer position) {
        return concertSeatJpaRepository.findByConcertScheduleIdAndPosition(concertScheduleId, position);
    }

    @Override
    public ConcertSchedule findConcertSchedule(Long concertScheduleId) {
        return concertScheduleJpaRepository.findById(concertScheduleId)
                .orElseThrow(() -> new ApiException(ErrorCode.E404, LogLevel.INFO, "ConcertSchedule not found concertScheduleId = " + concertScheduleId));
    }

    @Override
    public Integer updateReservationStatus(ReservationStatus status, Long concertScheduleId, Long seatId) {
        return reservationJpaRepository.updateReservationStatus(status, concertScheduleId, seatId);
    }

    @Override
    public Reservation saveReservation(Reservation reservation) {
        return reservationJpaRepository.save(reservation);
    }

    @Override
    public List<Reservation> findReservationReleaseTarget(LocalDateTime expiredAt) {
        return concertQueryRepository.findReservationReleaseTarget(expiredAt);
    }

    @Override
    public void deleteReservation(List<Reservation> reservations) {
        reservationJpaRepository.deleteAllInBatch(reservations);
    }

    @Override
    public void deletePaymentBy(List<Long> reservationIds) {
        paymentJpaRepository.deleteAllBy(reservationIds);
    }

    @Override
    public void deleteSeats(List<Long> seats) {
        concertSeatJpaRepository.deleteAllById(seats);
    }

    @Override
    public List<Reservation> findBy(Long concertScheduleId) {
        return reservationJpaRepository.findByConcertScheduleId(concertScheduleId);
    }

}
