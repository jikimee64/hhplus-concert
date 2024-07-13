package hhplus.concert.infra.persistence;

import hhplus.concert.domain.ConcertSchedule;
import hhplus.concert.domain.TotalSeatStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConcertScheduleJpaRepository extends JpaRepository<ConcertSchedule, Long> {
    List<ConcertSchedule> findByConcertIdAndStatus(Long concertId, TotalSeatStatus status);
}
