package hhplus.concert.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private Long concertSeatId;

    private ReservationStatus status;

    public Reservation(Long id, Long userId, Long concertSeatId, ReservationStatus status) {
        this.id = id;
        this.userId = userId;
        this.concertSeatId = concertSeatId;
        this.status = status;
    }

    public Reservation(Long userId, Long concertSeatId, ReservationStatus status) {
        this(null, userId, concertSeatId, status);
    }
}
