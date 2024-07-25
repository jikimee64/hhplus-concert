package hhplus.concert.domain.concert;

import hhplus.concert.IntegrationTest;
import hhplus.concert.interfaces.api.support.ApiException;
import hhplus.concert.interfaces.api.support.error.ErrorCode;
import hhplus.concert.infra.persistence.ConcertJpaRepository;
import hhplus.concert.infra.persistence.ConcertScheduleJpaRepository;
import hhplus.concert.infra.persistence.ConcertSeatJpaRepository;
import hhplus.concert.infra.persistence.ReservationJpaRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class ConcertManagerTest extends IntegrationTest {

    @Autowired
    private ConcertManager concertManager;

    @Autowired
    private ConcertJpaRepository concertJpaRepository;

    @Autowired
    private ConcertScheduleJpaRepository concertScheduleJpaRepository;

    @Autowired
    private ReservationJpaRepository reservationJpaRepository;

    @Autowired
    private ConcertSeatJpaRepository concertSeatJpaRepository;

    @Autowired
    private EntityManager entityManager;

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
                new ConcertSeat(1L, 10000, 1)
        );
        int seatPosition = 1;
        int seatAmount = 10000;
        // when
        Reservation reservation = concertManager.reserveSeat(savedConcertSchedule, userId, savedConcertSeat.getId());

        // then
        entityManager.flush();
        entityManager.clear();
        ConcertSchedule concertSchedule = concertScheduleJpaRepository.findById(savedConcertSchedule.getId()).get();

        assertAll(
                () -> assertThat(reservation.getUserId()).isEqualTo(userId),
                () -> assertThat(reservation.getConcertScheduleId()).isEqualTo(savedConcertSchedule.getId()),
                () -> assertThat(reservation.getConcertTitle()).isEqualTo(savedConcert.getTitle()),
                () -> assertThat(reservation.getConcertOpenDate()).isEqualTo(savedConcertSchedule.getOpenDate()),
                () -> assertThat(reservation.getConcertStartAt()).isEqualTo(savedConcertSchedule.getStartAt()),
                () -> assertThat(reservation.getConcertEndAt()).isEqualTo(savedConcertSchedule.getEndAt()),
                () -> assertThat(reservation.getSeatAmount()).isEqualTo(seatAmount),
                () -> assertThat(reservation.getSeatPosition()).isEqualTo(seatPosition),
                () -> assertThat(reservation.getStatus()).isEqualTo(ReservationStatus.TEMP_RESERVED)
        );
    }

    @Test
    void 콘서트_스케줄의_날짜의_좌석이_임시예약_혹은_예약된_상태인_경우_예외가_발생한다() {
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

        int seatPosition = 1;
        int seatAmount = 10000;
        ConcertSeat concertSeat = concertSeatJpaRepository.save(
                new ConcertSeat(savedConcertSchedule.getId(), seatAmount, seatPosition)
        );

        reservationJpaRepository.save(
                Reservation.builder()
                        .userId(userId)
                        .concertScheduleId(savedConcertSchedule.getId())
                        .seatId(concertSeat.getId())
                        .build()
        );

        // when & then
        assertThatThrownBy(() -> concertManager.reserveSeat(savedConcertSchedule, userId, concertSeat.getId()))
                .isInstanceOf(ApiException.class)
                .hasMessage(ErrorCode.E002.getMessage());
    }

    @Test
    void 콘서트_스케줄의_날짜의_좌석이_만석인_경우_예외가_발생한다() {
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

        int seatPosition = 1;
        int seatAmount = 10000;
        ConcertSeat concertSeat = concertSeatJpaRepository.save(
                new ConcertSeat(savedConcertSchedule.getId(), seatAmount, seatPosition)
        );

        // when & then
        assertThatThrownBy(() -> concertManager.reserveSeat(savedConcertSchedule, userId, concertSeat.getId()))
                .isInstanceOf(ApiException.class)
                .hasMessage(ErrorCode.E007.getMessage());
    }

}
