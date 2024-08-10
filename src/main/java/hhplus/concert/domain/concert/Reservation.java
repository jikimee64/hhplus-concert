package hhplus.concert.domain.concert;

import hhplus.concert.interfaces.api.support.ApiException;
import hhplus.concert.interfaces.api.support.error.ErrorCode;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import org.springframework.boot.logging.LogLevel;

@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(
        name = "reservation",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_reservation_idx_1",
                        columnNames = {"concert_schedule_id", "seat_id"})
        }
)
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

    public void validateNotAlreadyReserved(Long concertScheduleId, Long seatId) {
        if (isReserved() || isTempReserved()) {
            throw new ApiException(ErrorCode.E002, LogLevel.INFO, "concertScheduleId = " + concertScheduleId + ", seatId = " + seatId);
        }
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

    public LocalDateTime getConcertStartAt() {
        return concertStartAt;
    }

    public LocalDateTime getConcertEndAt() {
        return concertEndAt;
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

    public LocalDateTime getReservedAt() {
        return reservedAt;
    }
}
