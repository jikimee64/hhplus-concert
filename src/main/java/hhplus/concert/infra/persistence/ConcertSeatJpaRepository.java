package hhplus.concert.infra.persistence;

import hhplus.concert.domain.ConcertSeat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConcertSeatJpaRepository extends JpaRepository<ConcertSeat, Integer> {
}
