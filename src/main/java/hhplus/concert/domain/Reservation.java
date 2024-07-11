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

    private LocalDateTime reservedAt;

    public Reservation(Long id, Long userId, Long seatId, ReservationStatus status) {
        this.id = id;
        this.userId = userId;
        this.seatId = seatId;
        this.status = status;
    }

    public Reservation(Long userId, Long seatId, ReservationStatus status) {
        this(null, userId, seatId, status);
    }

    public Reservation(Long userId, Long seatId, ReservationStatus status, LocalDateTime reservedAt) {
        this.userId = userId;
        this.seatId = seatId;
        this.status = status;
        this.reservedAt = reservedAt;
    }

    public boolean isUserAmountSufficient(Integer userAmount) {
        return this.seatAmount <= userAmount;
    }

    public boolean isReserved() {
        return this.status == ReservationStatus.RESERVED;
    }

    public boolean isTempReserved() {
        return this.status == ReservationStatus.TEMP_RESERVED;
    }

    public Long getId() {
        return id;
    }

    public Long getSeatId() {
        return seatId;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getConcertScheduleId() {
        return concertScheduleId;
    }

    public String getConcertTitle() {
        return concertTitle;
    }

    public LocalDate getConcertOpenDate() {
        return concertOpenDate;
    }

    public Integer getSeatPosition() {
        return seatPosition;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public Integer getSeatAmount() {
        return seatAmount;
    }
}
