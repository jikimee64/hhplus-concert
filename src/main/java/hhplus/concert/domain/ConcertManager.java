package hhplus.concert.domain;

import hhplus.concert.interfaces.api.support.ApiException;
import hhplus.concert.interfaces.api.support.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ConcertManager {

    private final ConcertRepository concertRepository;

    /**
     * 좌석 임시 예약
     * - 콘서트 스케줄의 개최 날짜가 요청값과 일치하는지 확인하여 일치하지 않을 경우 예외 처리
     * - 해당 콘서트 스케줄에 해당하는 좌석번호 데이터가 좌석 테이블에 존재할 경우 예약 여부 확인하여 존재하면 예외 처리
     * - 존재하지 않을 경우 좌석 테이블에 좌석번호 데이터 저장
     * - 예약 데이터
     */
    @Transactional
    public Reservation reserveSeat(Long concertScheduleId, LocalDate concertOpenDate, Long userId, Integer seatPosition, Integer seatAmount) {
        ConcertSchedule concertSchedule = concertRepository.findConcertSchedule(concertScheduleId);
        if (isEqualConcertOpenDate(concertOpenDate, concertSchedule.getOpenDate())) {
            throw new ApiException(ErrorCode.E006, "requestConcertOpenDate = " + concertOpenDate + ", concertOpenDate = " + concertSchedule.getOpenDate());
        }

        ConcertSeat concertSeat;
        // 좌석 테이블에 데이터가 존재할 경우 예약 테이블에 예약이 중복으로 존재하는지 검사
        Optional<ConcertSeat> optConcertSeat = concertRepository.findSeatBy(concertScheduleId, seatPosition);
        if (optConcertSeat.isPresent()) {
            concertSeat = optConcertSeat.get();
            if (isSeatReserved(concertScheduleId, concertSeat.getId())) {
                throw new ApiException(ErrorCode.E002, "concertScheduleId = " + concertScheduleId + ", seatId = " + concertSeat.getId());
            }
        } else {
            concertSeat = concertRepository.saveSeat(
                    new ConcertSeat(concertScheduleId, seatAmount, seatPosition)
            );
        }
        return saveReservation(concertSchedule, concertSeat, userId);
    }

    private boolean isSeatReserved(Long concertScheduleId, Long seatId) {
        return concertRepository.findReservation(concertScheduleId, seatId).isPresent();
    }

    private boolean isEqualConcertOpenDate(LocalDate requestConcertOpenDate, LocalDate concertSchedule) {
        return !requestConcertOpenDate.equals(concertSchedule);
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
