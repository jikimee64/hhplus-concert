package hhplus.concert.infra.persistence;

import hhplus.concert.domain.concert.ConcertSeat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ConcertSeatJpaRepository extends JpaRepository<ConcertSeat, Long> {
    Optional<ConcertSeat> findByConcertScheduleIdAndPosition(Long concertScheduleId, Integer position);
}