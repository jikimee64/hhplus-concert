package hhplus.concert.application;

import hhplus.concert.application.dto.ConcertScheduleResult;
import hhplus.concert.application.dto.ReservationSeatCommand;
import hhplus.concert.application.dto.SeatResult;
import hhplus.concert.domain.concert.*;
import hhplus.concert.domain.userqueue.UserQueueManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ConcertService {

    private final UserQueueManager userQueueManager;
    private final ConcertFinder concertFinder;
    private final ConcertManager concertManager;

    public List<ConcertScheduleResult> selectConcertSchedule(String token, Long concertId, String status) {
        userQueueManager.validateTopExpiredBy(token);
        List<ConcertSchedule> concertSchedules = concertFinder.selectConcertScheduleBy(concertId, status);
        return ConcertScheduleResult.from(concertSchedules);
    }

    public List<SeatResult> selectSeat(String token, Long concertScheduleId) {
        userQueueManager.validateTopExpiredBy(token);
        List<SeatDto> seatDtos = concertFinder.selectSeatBy(concertScheduleId);
        return SeatResult.from(seatDtos);
    }

    public void reserveSeat(String token, ReservationSeatCommand command) {
        userQueueManager.validateTopExpiredBy(token);
        concertManager.reserveSeat(
                command.concertScheduleId(),
                command.concertOpenDate(),
                command.userId(),
                command.seatPosition(),
                command.seatAmount()
        );
    }
}
