package hhplus.concert.domain.concert;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class ConcertScheduleTest {

    @Test
    void 콘서트_스케줄의_좌석_상태값을_SOLD_OUT으로_변경한다() {
        // given
        Concert savedConcert1 = new Concert("콘서트1");
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDate localDate = LocalDate.parse("2024-01-01", dateFormatter);
        LocalDateTime localDateTime = LocalDateTime.parse("2024-01-01 10:00:00", dateTimeFormatter);

        ConcertSchedule concertSchedule = new ConcertSchedule(savedConcert1, localDate, localDateTime, localDateTime.plusHours(1L), 50, TotalSeatStatus.SOLD_OUT);

        // when
        concertSchedule.updateTotalSeatStatusSoldOut();

        // then
        assertThat(concertSchedule.getStatus()).isEqualTo(TotalSeatStatus.SOLD_OUT);
    }

}
