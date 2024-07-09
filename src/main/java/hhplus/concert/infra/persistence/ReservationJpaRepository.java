package hhplus.concert.infra.persistence;

import hhplus.concert.domain.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationJpaRepository extends JpaRepository<Reservation, Long>{
}
