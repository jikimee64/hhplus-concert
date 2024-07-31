package hhplus.concert.infra.persistence;

import hhplus.concert.domain.concert.*;
import hhplus.concert.domain.concert.dto.SeatQueryDto;
import hhplus.concert.interfaces.api.support.ApiException;
import hhplus.concert.interfaces.api.support.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.logging.LogLevel;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class ConcertRepositoryImpl implements ConcertRepository {

    private final ReservationJpaRepository reservationJpaRepository;
    private final PaymentJpaRepository paymentJpaRepository;
    private final ConcertScheduleJpaRepository concertScheduleJpaRepository;
    private final ConcertSeatJpaRepository concertSeatJpaRepository;
    private final ConcertScheduleRedisRepository concertScheduleRedisRepository;
    private final ConcertQueryRepository concertQueryRepository;

    @Override
    public List<ConcertSchedule> findConcertScheduleBy(Long concertId, TotalSeatStatus status) {
        List<ConcertSchedule> cachedConcertSchedules = concertScheduleRedisRepository.getConcertSchedules(concertId);
        if (cachedConcertSchedules == null) {
            List<ConcertSchedule> concertSchedules = concertScheduleJpaRepository.findByConcertIdAndStatus(concertId, status);
            concertScheduleRedisRepository.saveConcertSchedules(concertId, concertSchedules);
            return concertSchedules;
        } else {
            return cachedConcertSchedules;
        }
    }

    @Override
    public List<ConcertSchedule> findConcertSchedules(List<Long> concertScheduleIds) {
        return concertScheduleJpaRepository.findAllByIdIn(concertScheduleIds);
    }

    @Override
    public Optional<Reservation> findReservation(Long concertScheduleId, Long seatId) {
        return reservationJpaRepository.findByConcertScheduleIdAndSeatId(concertScheduleId, seatId);
    }

    @Override
    public Optional<Reservation> findReservationWithLock(Long concertScheduleId, Long seatId) {
        return reservationJpaRepository.findReservationWithLock(concertScheduleId, seatId);
    }

    @Override
    public List<SeatQueryDto> findConcertSeat(Long concertScheduleId) {
        return concertQueryRepository.findConcertSeat(concertScheduleId);
    }

    public ConcertSeat findSeat(Long seatId) {
        return concertSeatJpaRepository.findById(seatId)
            .orElseThrow(() -> new ApiException(ErrorCode.E404, LogLevel.INFO, "ConcertSeat not found seatId = " + seatId));
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
    public List<Reservation> findReservations(List<Long> concertScheduleIds) {
        return reservationJpaRepository.findByConcertScheduleIn(concertScheduleIds);
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

    @Override
    public void evictCachedConcertSchedule(Long concertId) {
        concertScheduleRedisRepository.deleteAllConcertSchedules(concertId);
    }

}
