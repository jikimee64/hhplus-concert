package hhplus.concert.application.concert;

import hhplus.concert.application.concert.dto.ConcertScheduleResult;
import hhplus.concert.application.concert.dto.ReservationSeatCommand;
import hhplus.concert.application.concert.dto.SeatResult;
import hhplus.concert.domain.concert.*;
import hhplus.concert.domain.concert.dto.SeatDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConcertService {

    private final ConcertFinder concertFinder;
    private final ConcertManager concertManager;

    public List<ConcertScheduleResult> selectConcertSchedule(String token, Long concertId, String status) {
        List<ConcertSchedule> concertSchedules = concertFinder.selectConcertScheduleBy(concertId, status);
        return ConcertScheduleResult.from(concertSchedules);
    }

    public List<SeatResult> selectSeat(Long concertScheduleId) {
        List<SeatDto> seatDtos = concertFinder.selectSeatBy(concertScheduleId);
        return SeatResult.from(seatDtos);
    }

    public void reserveSeat(ReservationSeatCommand command) {
        ConcertSchedule concertSchedule = concertFinder.getConcertSchedule(command.concertScheduleId());
        concertManager.reserveSeat(
                concertSchedule,
                command.userId(),
                command.seatId()
        );
    }
}
