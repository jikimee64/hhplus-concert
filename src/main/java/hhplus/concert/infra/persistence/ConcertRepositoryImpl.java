package hhplus.concert.infra.persistence;

import hhplus.concert.api.support.ApiException;
import hhplus.concert.api.support.error.ErrorCode;
import hhplus.concert.domain.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ConcertRepositoryImpl implements ConcertRepository {

    private final ReservationJpaRepository reservationJpaRepository;
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
    public ConcertSeat findSeat(Long seatId) {
        return concertSeatJpaRepository.findById(seatId)
                .orElseThrow(() -> new ApiException(ErrorCode.E404, "Seat not found seatId = " + seatId));
    }

    @Override
    public ConcertSchedule findConcertSchedule(Long concertScheduleId) {
        return concertScheduleJpaRepository.findById(concertScheduleId)
                .orElseThrow(() -> new ApiException(ErrorCode.E404, "ConcertSchedule not found concertScheduleId = " + concertScheduleId));
    }

    @Override
    public Reservation saveReservation(Reservation reservation) {
        return reservationJpaRepository.save(reservation);
    }

}
