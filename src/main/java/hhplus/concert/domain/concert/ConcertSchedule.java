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

    @Column(name = "total_seat_status")
    @Enumerated(EnumType.STRING)
    private TotalSeatStatus status;

    @Version
    private Integer version;

    public ConcertSchedule(Long id, Concert concert, LocalDate openDate, LocalDateTime startAt, LocalDateTime endAt, Integer totalSeat, TotalSeatStatus status) {
        this.id = id;
        this.concert = concert;
        this.openDate = openDate;
        this.startAt = startAt;
        this.endAt = endAt;
        this.totalSeat = totalSeat;
        this.status = status;
    }

    public ConcertSchedule(Concert concert, LocalDate openDate, LocalDateTime startAt, LocalDateTime endAt, Integer totalSeat, TotalSeatStatus status) {
        this(null, concert, openDate, startAt, endAt, totalSeat, status);
    }

    public ConcertSchedule(Concert concert, LocalDate openDate, LocalDateTime startAt, LocalDateTime endAt, Integer totalSeat) {
        this(concert, openDate, startAt, endAt, totalSeat, TotalSeatStatus.AVAILABLE);
    }

    public void isTotalSeatSoldOut(){
        if(this.status == TotalSeatStatus.SOLD_OUT){
            throw new ApiException(ErrorCode.E007, LogLevel.INFO);
        }
    }

    public void updateTotalSeatStatusSoldOut(){
        this.status = TotalSeatStatus.SOLD_OUT;
    }

    public void updateTotalSeatStatusAvailable(){
        this.status = TotalSeatStatus.AVAILABLE;
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

}
