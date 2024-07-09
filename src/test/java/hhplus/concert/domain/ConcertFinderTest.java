package hhplus.concert.domain;

import hhplus.concert.IntegrationTest;
import hhplus.concert.infra.persistence.ConcertScheduleJpaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.tuple;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class ConcertFinderTest extends IntegrationTest {

    @Autowired
    private ConcertScheduleJpaRepository concertScheduleJpaRepository;

    @Autowired
    private ConcertFinder concertFinder;

    @Test
    void 콘서트_스케줄의_예약_가능한_날짜를_조회한다(){
        // given
        Long concertId = 1L;
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDate localDate = LocalDate.parse("2024-01-01", dateFormatter);
        LocalDateTime localDateTime = LocalDateTime.parse("2024-01-01 10:00:00", dateTimeFormatter);

        concertScheduleJpaRepository.saveAll(
                List.of(
                    new ConcertSchedule(concertId, localDate, localDateTime, localDateTime.plusHours(1L), 50, TotalSeatStatus.SOLD_OUT),
                    new ConcertSchedule(concertId, localDate, localDateTime.plusHours(1L), localDateTime.plusHours(2L), 50, TotalSeatStatus.AVAILABLE),
                    new ConcertSchedule(concertId, localDate, localDateTime.plusHours(2L), localDateTime.plusHours(3L), 50, TotalSeatStatus.SOLD_OUT)
                )
        );

        // when
        List<ConcertSchedule> concertSchedules = concertFinder.selectAvailableReservation();

        // then
        assertThat(concertSchedules).hasSize(1)
                .extracting("startAt", "endAt", "status")
                .containsExactly(
                        tuple(localDateTime.plusHours(1L), localDateTime.plusHours(2L), TotalSeatStatus.AVAILABLE)
                );
    }

}
