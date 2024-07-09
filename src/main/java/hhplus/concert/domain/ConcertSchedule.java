package hhplus.concert.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ConcertSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long concertId;

    private LocalDate openDate;

    private LocalDateTime startAt;

    private LocalDateTime endAt;

    private Integer totalSeat;

    @Column(name = "total_seat_status")
    @Enumerated(EnumType.STRING)
    private TotalSeatStatus status;

    public ConcertSchedule(Long id, Long concertId, LocalDate openDate, LocalDateTime startAt, LocalDateTime endAt, Integer totalSeat, TotalSeatStatus status) {
        this.id = id;
        this.concertId = concertId;
        this.openDate = openDate;
        this.startAt = startAt;
        this.endAt = endAt;
        this.totalSeat = totalSeat;
        this.status = status;
    }

    public ConcertSchedule(Long concertId, LocalDate openDate, LocalDateTime startAt, LocalDateTime endAt, Integer totalSeat, TotalSeatStatus status) {
        this(null, concertId, openDate, startAt, endAt, totalSeat, status);
    }

    public ConcertSchedule(Long concertId, LocalDate openDate, LocalDateTime startAt, LocalDateTime endAt, Integer totalSeat) {
        this(concertId, openDate, startAt, endAt, totalSeat, TotalSeatStatus.AVAILABLE);
    }
}
