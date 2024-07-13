package hhplus.concert.domain.concert;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ConcertSeat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long concertScheduleId;

    private Integer amount;

    private Integer position;

    public ConcertSeat(Long id, Long concertScheduleId, Integer amount, Integer position) {
        this.id = id;
        this.concertScheduleId = concertScheduleId;
        this.amount = amount;
        this.position = position;
    }

    public ConcertSeat(Long concertScheduleId, Integer amount, Integer position) {
        this(null, concertScheduleId, amount, position);
    }

    public Long getId() {
        return id;
    }

    public Long getConcertScheduleId() {
        return concertScheduleId;
    }

    public Integer getAmount() {
        return amount;
    }

    public Integer getPosition() {
        return position;
    }
}
