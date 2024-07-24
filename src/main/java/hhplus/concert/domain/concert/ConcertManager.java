package hhplus.concert.domain.concert;

import hhplus.concert.interfaces.api.support.ApiException;
import hhplus.concert.interfaces.api.support.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.logging.LogLevel;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@Component
@Transactional
@RequiredArgsConstructor
public class ConcertManager {

    private final ConcertRepository concertRepository;

    public Reservation reserveSeat(ConcertSchedule concertSchedule, Long userId, Long seatId) {
        Long concertScheduleId = concertSchedule.getId();
        if (isSeatReserved(concertScheduleId, seatId)) {
            throw new ApiException(ErrorCode.E002, LogLevel.INFO, "concertScheduleId = " + concertScheduleId + ", seatId = " + seatId);
        }
        concertSchedule.increaseReservedSeat();
        ConcertSeat concertSeat = concertRepository.findSeat(seatId);
        return saveReservation(concertSchedule, concertSeat, userId);
    }

    private boolean isSeatReserved(Long concertScheduleId, Long seatId) {
        return concertRepository.findReservation(concertScheduleId, seatId).isPresent();
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
