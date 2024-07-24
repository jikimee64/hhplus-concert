package hhplus.concert.domain.concert;

import hhplus.concert.interfaces.api.support.ApiException;
import hhplus.concert.interfaces.api.support.error.ErrorCode;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.boot.logging.LogLevel;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ConcertSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "concert_id")
    private Concert concert;

    private LocalDate openDate;

    private LocalDateTime startAt;

    private LocalDateTime endAt;

    private Integer totalSeat;

    private Integer reservationSeat;

    @Column(name = "total_seat_status")
    @Enumerated(EnumType.STRING)
    private TotalSeatStatus status;

    public ConcertSchedule(Long id, Concert concert, LocalDate openDate, LocalDateTime startAt, LocalDateTime endAt, Integer totalSeat, Integer reservationSeat, TotalSeatStatus status) {
        this.id = id;
        this.concert = concert;
        this.openDate = openDate;
        this.startAt = startAt;
        this.endAt = endAt;
        this.totalSeat = totalSeat;
        this.reservationSeat = reservationSeat;
        this.status = status;
    }

    public ConcertSchedule(Concert concert, LocalDate openDate, LocalDateTime startAt, LocalDateTime endAt, Integer totalSeat, Integer reservationSeat, TotalSeatStatus status) {
        this(null, concert, openDate, startAt, endAt, totalSeat, reservationSeat, status);
    }

    public ConcertSchedule(Concert concert, LocalDate openDate, LocalDateTime startAt, LocalDateTime endAt, Integer totalSeat, TotalSeatStatus status) {
        this(null, concert, openDate, startAt, endAt, totalSeat, 0, status);
    }

    public ConcertSchedule(Concert concert, LocalDate openDate, LocalDateTime startAt, LocalDateTime endAt, Integer totalSeat) {
        this(concert, openDate, startAt, endAt, totalSeat, TotalSeatStatus.AVAILABLE);
    }

    public void increaseReservedSeat() {
        if (reservationSeat >= totalSeat) {
            throw new ApiException(ErrorCode.E007, LogLevel.INFO, "reservationSeat: " + reservationSeat + ", totalSeat: " + totalSeat);
        }
        reservationSeat++;
    }

    public void updateTotalSeatStatusSoldOut(){
        this.status = TotalSeatStatus.SOLD_OUT;
    }

    public Long getId() {
        return id;
    }

    public Concert getConcert() {
        return concert;
    }

    public TotalSeatStatus getStatus() {
        return status;
    }

    public Integer getTotalSeat() {
        return totalSeat;
    }

    public LocalDateTime getEndAt() {
        return endAt;
    }

    public LocalDateTime getStartAt() {
        return startAt;
    }

    public LocalDate getOpenDate() {
        return openDate;
    }

    public Integer getReservationSeat() {
        return reservationSeat;
    }
}
