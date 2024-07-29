package hhplus.concert.domain.concert;

import hhplus.concert.IntegrationTest;
import hhplus.concert.domain.concert.dto.SeatDto;
import hhplus.concert.infra.persistence.ConcertJpaRepository;
import hhplus.concert.infra.persistence.ConcertScheduleJpaRepository;
import hhplus.concert.infra.persistence.ConcertSeatJpaRepository;
import hhplus.concert.infra.persistence.ReservationJpaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.tuple;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class ConcertFinderTest extends IntegrationTest {

    @Autowired
    private ConcertJpaRepository concertJpaRepository;

    @Autowired
    private ConcertScheduleJpaRepository concertScheduleJpaRepository;

    @Autowired
    private ConcertSeatJpaRepository concertSeatJpaRepository;

    @Autowired
    private ReservationJpaRepository reservationJpaRepository;

    @Autowired
    private ConcertFinder concertFinder;

    @Test
    void 콘서트의_예약_가능한_콘서트_스케줄을_조회한다() {
        // given
        Concert savedConcert1 = concertJpaRepository.save(new Concert("콘서트1"));
        Concert savedConcert2 = concertJpaRepository.save(new Concert("콘서트2"));
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDate localDate = LocalDate.parse("2024-01-01", dateFormatter);
        LocalDateTime localDateTime = LocalDateTime.parse("2024-01-01 10:00:00", dateTimeFormatter);

        concertScheduleJpaRepository.saveAll(
                List.of(
                        new ConcertSchedule(savedConcert1, localDate, localDateTime, localDateTime.plusHours(1L), 50, TotalSeatStatus.SOLD_OUT),
                        new ConcertSchedule(savedConcert1, localDate, localDateTime.plusHours(1L), localDateTime.plusHours(2L), 50, TotalSeatStatus.AVAILABLE),
                        new ConcertSchedule(savedConcert1, localDate, localDateTime.plusHours(2L), localDateTime.plusHours(3L), 50, TotalSeatStatus.SOLD_OUT),
                        new ConcertSchedule(savedConcert2, localDate, localDateTime.plusHours(2L), localDateTime.plusHours(3L), 50, TotalSeatStatus.AVAILABLE)
                )
        );

        // when
        List<ConcertSchedule> concertSchedules = concertFinder.selectConcertScheduleBy(savedConcert1.getId(), "AVAILABLE");

        // then
        assertThat(concertSchedules).hasSize(1)
                .extracting("startAt", "endAt", "status")
                .containsExactly(
                        tuple(localDateTime.plusHours(1L), localDateTime.plusHours(2L), TotalSeatStatus.AVAILABLE)
                );
    }

    @Test
    void 콘서트_스케줄의_전체_좌석을_조회한다() {
        // given
        Concert savedConcert1 = concertJpaRepository.save(new Concert("콘서트1"));
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDate openDate = LocalDate.parse("2024-01-01", dateFormatter);
        LocalDateTime localDateTime = LocalDateTime.parse("2024-01-01 10:00:00", dateTimeFormatter);
        int totalSeat = 50;

        ConcertSchedule savedConcertSchedule = concertScheduleJpaRepository.save(
                new ConcertSchedule(savedConcert1, openDate, localDateTime, localDateTime.plusHours(1L), totalSeat, TotalSeatStatus.AVAILABLE)
        );

        ConcertSeat savedConcertSeat = concertSeatJpaRepository.save(
                new ConcertSeat(savedConcertSchedule.getId(), 10000, 2)
        );

        reservationJpaRepository.save(
                new Reservation(1L, savedConcertSeat.getId(), ReservationStatus.RESERVED)
        );

        // when
        List<SeatDto> seatDtos = concertFinder.selectSeatBy(savedConcertSchedule.getId());

        // then
        assertAll(
                () -> assertThat(seatDtos).hasSize(totalSeat),
                () -> assertThat(seatDtos.get(1).amount()).isEqualTo(10000),
                () -> assertThat(seatDtos.get(1).position()).isEqualTo(2),
                () -> assertThat(seatDtos.get(1).status()).isEqualTo("RESERVED")
        );
    }

}
