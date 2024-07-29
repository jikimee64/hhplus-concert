package hhplus.concert.domain.concert;

import hhplus.concert.IntegrationTest;
import hhplus.concert.domain.pay.Payment;
import hhplus.concert.domain.pay.PaymentStatus;
import hhplus.concert.infra.persistence.ConcertSeatJpaRepository;
import hhplus.concert.infra.persistence.PaymentJpaRepository;
import hhplus.concert.infra.persistence.ReservationJpaRepository;
import hhplus.concert.support.holder.TestTimeHolder;
import hhplus.concert.support.holder.TimeHolder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class SeatAssignReleaseCheckerTest extends IntegrationTest {

    @Autowired
    private ReservationJpaRepository reservationJpaRepository;

    @Autowired
    private PaymentJpaRepository paymentJpaRepository;

    @Autowired
    private ConcertSeatJpaRepository concertSeatJpaRepository;

    @Autowired
    private ConcertRepository concertRepository;

    @Test
    void 좌석이_임시배정된_상태에서_5분_이내에_결제가_완료되지_않았을_경우_임시배정이_해제된다() {
        // given
        LocalDateTime now = LocalDateTime.of(2024, 1, 1, 0, 10, 0);
        TimeHolder testTimeHolder = new TestTimeHolder(now);
        SeatAssignReleaseChecker seatAssignReleaseChecker = new SeatAssignReleaseChecker(concertRepository, testTimeHolder);

        LocalDateTime expiredAt = LocalDateTime.of(2024, 1, 1, 0, 4, 59);
        LocalDateTime nonExpiredAt = LocalDateTime.of(2024, 1, 1, 0, 5, 1);
        ConcertSeat concertSeat1 = concertSeatJpaRepository.save(
                new ConcertSeat(1L, 0, 1)
        );
        reservationJpaRepository.save(
                new Reservation(1L, concertSeat1.getId(), ReservationStatus.TEMP_RESERVED, nonExpiredAt)
        );

        // 삭제되어야 할 좌석
        ConcertSeat concertSeat2 = concertSeatJpaRepository.save(
                new ConcertSeat(1L, 0, 2)
        );
        // 삭제되어야 할 예약
        reservationJpaRepository.save(
                new Reservation(1L, concertSeat2.getId(), ReservationStatus.TEMP_RESERVED, expiredAt)
        );

        // 삭제되어야 할 좌석
        ConcertSeat concertSeat3 = concertSeatJpaRepository.save(
                new ConcertSeat(1L, 0, 3)
        );
        // 삭제되어야 할 예약
        Reservation savedReservation = reservationJpaRepository.save(
                new Reservation(1L, concertSeat3.getId(), ReservationStatus.TEMP_RESERVED, expiredAt)
        );
        // 삭제되어야 할 결제
        paymentJpaRepository.save(
                new Payment(1L, savedReservation.getId(), PaymentStatus.PROGRESS)
        );

        // when
        seatAssignReleaseChecker.release();

        // then
        List<Reservation> reservations = reservationJpaRepository.findAll();
        List<ConcertSeat> concertSeats = concertSeatJpaRepository.findAll();
        List<Payment> payments = paymentJpaRepository.findAll();
        assertAll(
                () -> assertThat(reservations).hasSize(1),
                () -> assertThat(concertSeats).hasSize(1),
                () -> assertThat(payments).hasSize(0)
        );
    }

}
