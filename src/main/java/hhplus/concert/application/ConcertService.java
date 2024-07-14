package hhplus.concert.application;

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

    public List<ConcertSchedule> selectConcertSchedule(String token, Long concertId, String status){
        return concertFinder.selectConcertScheduleBy(concertId, status);
    }

    public List<SeatDto> selectSeat(String token, Long concertScheduleId){
        return concertFinder.selectSeatBy(concertScheduleId);
    }

    public Reservation reserveSeat(String token, LocalDate concertOpenDate, Long concertScheduleId, Long userId, Integer seatPosition, Integer seatAmount){
        return concertManager.reserveSeat(concertScheduleId, concertOpenDate, userId, seatPosition, seatAmount);
    }
}
