package hhplus.concert.domain;

import hhplus.concert.IntegrationTest;
import hhplus.concert.infra.persistence.ConcertJpaRepository;
import hhplus.concert.infra.persistence.ConcertScheduleJpaRepository;
import hhplus.concert.infra.persistence.ConcertSeatJpaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@Transactional
class ConcertManagerTest extends IntegrationTest {

    @Autowired
    private ConcertManager concertManager;

    @Autowired
    private ConcertJpaRepository concertJpaRepository;

    @Autowired
    private ConcertScheduleJpaRepository concertScheduleJpaRepository;

    @Autowired
    private ConcertSeatJpaRepository concertSeatJpaRepository;

    @Test
    void 콘서트_스케줄의_날짜에_좌석을_임시예약한다() {
        // given
        LocalDate concertOpenDate = LocalDate.of(2024, 1, 1);
        Long userId = 1L;
        Concert savedConcert = concertJpaRepository.save(
                new Concert(1L, "")
        );
        LocalDateTime now = LocalDateTime.now();
        ConcertSchedule savedConcertSchedule = concertScheduleJpaRepository.save(
                new ConcertSchedule(savedConcert, concertOpenDate, now.plusHours(1L), now.plusHours(2L), 50, TotalSeatStatus.AVAILABLE)
        );
        ConcertSeat savedConcertSeat = concertSeatJpaRepository.save(
                new ConcertSeat(savedConcertSchedule.getId(), 10000, 2)
        );

        // when
        Reservation reservation = concertManager.reserveSeat(savedConcertSchedule.getId(), concertOpenDate, userId, savedConcertSeat.getId());

        // then
        assertAll(
                () -> assertThat(reservation.getStatus()).isEqualTo(ReservationStatus.TEMP_RESERVED),
                () -> assertThat(reservation.getSeatId()).isEqualTo(savedConcertSeat.getId())
        );
    }

    //TODO: 이미 선점된 좌석인 경우 예외처리 테스트

    //TODO: 요청값으로 들어온 날짜와 콘서트 날짜가 다를 경우 예외처리 테스트

}
