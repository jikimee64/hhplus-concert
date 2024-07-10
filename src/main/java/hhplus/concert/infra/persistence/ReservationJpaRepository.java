package hhplus.concert.infra.persistence;

import hhplus.concert.domain.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReservationJpaRepository extends JpaRepository<Reservation, Long> {
    Optional<Reservation> findByConcertScheduleIdAndSeatId(Long concertScheduleId, Long seatId);
}
