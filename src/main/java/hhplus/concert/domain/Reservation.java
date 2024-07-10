package hhplus.concert.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private Long concertScheduleId;

    private Long seatId;

    private String concertTitle;

    private LocalDate concertOpenDate;

    private LocalDateTime concertStartAt;

    private LocalDateTime concertEndAt;

    private Integer seatAmount;

    private Integer seatPosition;

    private ReservationStatus status;

    public Reservation(Long id, Long userId, Long seatId, ReservationStatus status) {
        this.id = id;
        this.userId = userId;
        this.seatId = seatId;
        this.status = status;
    }

    public Reservation(Long userId, Long concertSeatId, ReservationStatus status) {
        this(null, userId, concertSeatId, status);
    }

    public Long getId() {
        return id;
    }

    public Long getSeatId() {
        return seatId;
    }

    public ReservationStatus getStatus() {
        return status;
    }
}
