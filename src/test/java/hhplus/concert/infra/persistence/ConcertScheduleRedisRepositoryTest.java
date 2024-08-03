package hhplus.concert.infra.persistence;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import hhplus.concert.domain.concert.Concert;
import hhplus.concert.domain.concert.ConcertSchedule;
import hhplus.concert.domain.concert.TotalSeatStatus;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ConcertScheduleRedisRepositoryTest {

    @Autowired
    private ConcertScheduleRedisRepository concertScheduleRedisRepository;

    @Test
    void redisTemplate를_이용한_저장_삭제_조회_테스트(){
        LocalDate concertOpenDate = LocalDate.of(2024, 1, 1);
        Concert concert = new Concert(1L, "");
        LocalDateTime now = LocalDateTime.now();
        ConcertSchedule concertSchedule = new ConcertSchedule(concert, concertOpenDate, now.plusHours(1L), now.plusHours(2L), 50, TotalSeatStatus.AVAILABLE);

        List<ConcertSchedule> concertSchedules = List.of(
            concertSchedule
        );

        concertScheduleRedisRepository.saveConcertSchedules(concert.getId(), concertSchedules);
        Object getConcertSchedules = concertScheduleRedisRepository.getConcertSchedules(concert.getId());
        assertThat(getConcertSchedules).isNotNull();

        concertScheduleRedisRepository.deleteConcertSchedules(concert.getId());
        Object getConcertSchedules2 = concertScheduleRedisRepository.getConcertSchedules(concert.getId());
        assertThat(getConcertSchedules2).isNull();
    }

}
