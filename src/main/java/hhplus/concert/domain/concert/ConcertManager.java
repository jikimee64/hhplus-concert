package hhplus.concert.domain.concert;

import hhplus.concert.interfaces.api.support.ApiException;
import hhplus.concert.interfaces.api.support.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.logging.LogLevel;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@Component
@Transactional
@RequiredArgsConstructor
public class ConcertManager {

    private final ConcertRepository concertRepository;

    @Transactional
    @Retryable(
            retryFor = {ObjectOptimisticLockingFailureException.class},
            maxAttempts = 3,
            backoff = @Backoff(delay = 100)
    )
    public Reservation reserveSeat(ConcertSchedule concertSchedule, Long userId, Long seatId) {
        Long concertScheduleId = concertSchedule.getId();
        Optional<Reservation> reservation = concertRepository.findReservation(concertScheduleId, seatId);
        if (reservation.isPresent()) {
            throw new ApiException(ErrorCode.E002, LogLevel.INFO, "concertScheduleId = " + concertScheduleId + ", seatId = " + seatId);
        }
        ConcertSeat concertSeat = concertRepository.findSeat(seatId);
        return saveReservation(concertSchedule, concertSeat, userId);
    }

    public Reservation reserveSeatWithLock(ConcertSchedule concertSchedule, Long userId, Long seatId) {
        Long concertScheduleId = concertSchedule.getId();
        Optional<Reservation> reservation = concertRepository.findReservationWithLock(concertScheduleId, seatId);
        if (reservation.isPresent()) {
            throw new ApiException(ErrorCode.E002, LogLevel.INFO, "concertScheduleId = " + concertScheduleId + ", seatId = " + seatId);
        }
        ConcertSeat concertSeat = concertRepository.findSeat(seatId);
        return saveReservation(concertSchedule, concertSeat, userId);
    }

    private Reservation saveReservation(ConcertSchedule concertSchedule, ConcertSeat concertSeat, Long userId) {
        return concertRepository.saveReservation(
                Reservation.builder()
                        .userId(userId)
                        .concertScheduleId(concertSchedule.getId())
                        .seatId(concertSeat.getId())
                        .concertTitle(concertSchedule.getConcert().getTitle())
                        .concertOpenDate(concertSchedule.getOpenDate())
                        .concertStartAt(concertSchedule.getStartAt())
                        .concertEndAt(concertSchedule.getEndAt())
                        .seatAmount(concertSeat.getAmount())
                        .seatPosition(concertSeat.getPosition())
                        .status(ReservationStatus.TEMP_RESERVED)
                        .build()
        );
    }
}
