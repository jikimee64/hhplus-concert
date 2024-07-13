package hhplus.concert.infra.persistence;

import hhplus.concert.domain.concert.Reservation;
import hhplus.concert.domain.concert.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ReservationJpaRepository extends JpaRepository<Reservation, Long> {
    Optional<Reservation> findByConcertScheduleIdAndSeatId(Long concertScheduleId, Long seatId);
    List<Reservation> findByConcertScheduleId(Long concertScheduleId);

    @Modifying
    @Query("""
              UPDATE Reservation r SET r.status = :status
              WHERE r.concertScheduleId = :concertScheduleId and r.seatId = :seatId
            """)
    Integer updateReservationStatus(
            @Param("status") ReservationStatus status,
            @Param("concertScheduleId") Long concertScheduleId,
            @Param("seatId") Long seatId

    );
}
