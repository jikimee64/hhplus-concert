package hhplus.concert.infra.persistence;

import com.querydsl.jpa.impl.JPAQueryFactory;
import hhplus.concert.domain.QSeatQueryDto;
import hhplus.concert.domain.SeatQueryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static hhplus.concert.domain.QConcertSchedule.concertSchedule;
import static hhplus.concert.domain.QConcertSeat.concertSeat;
import static hhplus.concert.domain.QReservation.reservation;

@Repository
@RequiredArgsConstructor
public class ConcertQueryRepository {

    private final JPAQueryFactory queryFactory;

    public List<SeatQueryDto> findConcertSeat(Long concertScheduleId) {
        return queryFactory
                .select(
                        new QSeatQueryDto(
                                concertSeat.id,
                                concertSchedule.totalSeat,
                                concertSeat.position,
                                concertSeat.amount,
                                reservation.status
                        )
                )
                .from(concertSchedule)
                .leftJoin(concertSeat).on(concertSchedule.id.eq(concertSeat.concertScheduleId).and(concertSeat.id.isNotNull()))
                .leftJoin(reservation).on(concertSeat.id.eq(reservation.concertSeatId).and(reservation.id.isNotNull()))
                .where(
                        concertSchedule.id.eq(concertScheduleId)
                )
                .fetch();
    }

}
