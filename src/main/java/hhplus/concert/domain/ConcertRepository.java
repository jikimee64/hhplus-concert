package hhplus.concert.domain;

import java.util.List;

public interface ConcertRepository {
    List<ConcertSchedule> findConcertScheduleBy(Long concertId, TotalSeatStatus status);
    List<SeatQueryDto> findConcertSeat(Long concertScheduleId);
}
