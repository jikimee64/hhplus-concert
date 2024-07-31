package hhplus.concert.domain.concert;

import static java.util.stream.Collectors.groupingBy;

import hhplus.concert.support.holder.TimeHolder;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Transactional
@RequiredArgsConstructor
public class SeatAssignReleaseChecker {

    private final ConcertRepository concertRepository;
    private final TimeHolder timeHolder;

    public void release() {
        LocalDateTime expiredAt = timeHolder.currentDateTime().minusMinutes(5);
        List<Reservation> reservations = concertRepository.findReservationReleaseTarget(expiredAt);
        concertRepository.deleteReservation(reservations);
        concertRepository.deletePaymentBy(getReservationIds(reservations));
        concertRepository.deleteSeats(getSeatIds(reservations));

        List<Long> concertScheduleIds = reservations.stream()
            .map(Reservation::getConcertScheduleId)
            .distinct()
            .toList();
        List<ConcertSchedule> concertSchedules = concertRepository.findConcertSchedules(concertScheduleIds);

        Map<Long, List<Reservation>> collect = concertRepository.findReservations(concertScheduleIds).stream()
            .collect(groupingBy(Reservation::getConcertScheduleId));

        for (ConcertSchedule concertSchedule : concertSchedules) {
            List<Reservation> getReservations = collect.get(concertSchedule.getId());
            long reserved = getReservations.stream()
                .filter(reservation -> reservation.isReserved() || reservation.isTempReserved())
                .count();
            if (reserved == concertSchedule.getTotalSeat()) {
                concertSchedule.updateTotalSeatStatusAvailable();
                concertRepository.evictCachedConcertSchedule();
            }
        }
    }


    private List<Long> getReservationIds(List<Reservation> reservations) {
        return reservations.stream().map(Reservation::getId).toList();
    }

    private List<Long> getSeatIds(List<Reservation> reservations) {
        return reservations.stream().map(Reservation::getSeatId).toList();
    }

}
