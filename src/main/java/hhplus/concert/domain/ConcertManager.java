package hhplus.concert.domain;

import hhplus.concert.api.support.ApiException;
import hhplus.concert.api.support.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class ConcertManager {

    private final ConcertRepository concertRepository;

    public Reservation reserveSeat(Long concertScheduleId, LocalDate concertOpenDate, Long userId, Long seatId) {
        if (isSeatReserved(concertScheduleId, seatId)) {
            throw new ApiException(ErrorCode.E002, "concertScheduleId = " + concertScheduleId + ", seatId = " + seatId);
        }
        ConcertSchedule concertSchedule = concertRepository.findConcertSchedule(concertScheduleId);
        if (isEqualConcertOpenDate(concertOpenDate, concertSchedule.getOpenDate())) {
            throw new ApiException(ErrorCode.E006, "requestConcertOpenDate = " + concertOpenDate + ", concertOpenDate = " + concertSchedule.getOpenDate());
        }
        ConcertSeat concertSeat = concertRepository.findSeat(seatId);

        return generateReservation(concertSchedule, concertSeat, userId);
    }

    private boolean isSeatReserved(Long concertScheduleId, Long seatId) {
        return concertRepository.findReservation(concertScheduleId, seatId).isPresent();
    }

    private boolean isEqualConcertOpenDate(LocalDate requestConcertOpenDate, LocalDate concertSchedule) {
        return !requestConcertOpenDate.equals(concertSchedule);
    }

    private Reservation generateReservation(ConcertSchedule concertSchedule, ConcertSeat concertSeat, Long userId) {
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
