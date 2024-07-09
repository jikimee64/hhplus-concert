package hhplus.concert.infra.persistence;

import hhplus.concert.domain.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ConcertRepositoryImpl implements ConcertRepository {

    private final ConcertScheduleJpaRepository concertScheduleJpaRepository;
    private final ConcertQueryRepository concertQueryRepository;

    @Override
    public List<ConcertSchedule> findConcertScheduleBy(Long concertId, TotalSeatStatus status) {
        return concertScheduleJpaRepository.findByConcertIdAndStatus(concertId, status);
    }

    @Override
    public List<SeatQueryDto> findConcertSeat(Long concertScheduleId) {
        return concertQueryRepository.findConcertSeat(concertScheduleId);
    }

}
