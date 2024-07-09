package hhplus.concert.infra.persistence;

import hhplus.concert.domain.ConcertRepository;
import hhplus.concert.domain.ConcertSchedule;
import hhplus.concert.domain.TotalSeatStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ConcertRepositoryImpl implements ConcertRepository {

    private final ConcertScheduleJpaRepository concertScheduleJpaRepository;

    @Override
    public List<ConcertSchedule> findBy(TotalSeatStatus status) {
        return concertScheduleJpaRepository.findByStatus(status);
    }
}
