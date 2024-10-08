package hhplus.concert.domain.pay;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import hhplus.concert.IntegrationTest;
import hhplus.concert.domain.concert.Concert;
import hhplus.concert.domain.concert.ConcertSchedule;
import hhplus.concert.domain.concert.Reservation;
import hhplus.concert.domain.concert.ReservationStatus;
import hhplus.concert.domain.concert.TotalSeatStatus;
import hhplus.concert.domain.outbox.EventType;
import hhplus.concert.domain.outbox.MessageOutbox;
import hhplus.concert.domain.pay.dto.Receipt;
import hhplus.concert.domain.user.User;
import hhplus.concert.infra.outbox.MessageOutboxJpaRepository;
import hhplus.concert.infra.persistence.ActiveQueueRedisRepository;
import hhplus.concert.infra.persistence.ConcertJpaRepository;
import hhplus.concert.infra.persistence.ConcertScheduleJpaRepository;
import hhplus.concert.infra.persistence.ReservationJpaRepository;
import hhplus.concert.infra.persistence.UserJpaRepository;
import hhplus.concert.interfaces.api.support.ApiException;
import hhplus.concert.interfaces.api.support.error.ErrorCode;
import hhplus.concert.support.constant.ConcertTopic;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class PaymentFacadeTest extends IntegrationTest {

    @Autowired
    private UserJpaRepository userJpaRepository;

    @Autowired
    private ReservationJpaRepository reservationJpaRepository;

    @Autowired
    private ConcertJpaRepository concertJpaRepository;

    @Autowired
    private ActiveQueueRedisRepository activeQueueRedisRepository;

    @Autowired
    private ConcertScheduleJpaRepository concertScheduleJpaRepository;

    @Autowired
    private MessageOutboxJpaRepository messageOutboxJpaRepository;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private EntityManager entityManager;

    @Test
    void 유저의_결제_금액이_부족할_경우_결제에_실패한다() {
        // given
        User save = userJpaRepository.save(new User("user1", 1000));
        Long concertScheduleId = 1L;
        Long seatId = 1L;
        LocalDate concertOpenDate = LocalDate.now();
        reservationJpaRepository.save(
            Reservation.builder()
                .concertScheduleId(concertScheduleId)
                .seatId(seatId)
                .seatAmount(1001)
                .build()
        );

        // when & then
        assertThatThrownBy(() -> paymentService.pay("", save.getId(), concertScheduleId, seatId, concertOpenDate))
            .isInstanceOf(ApiException.class)
            .hasMessage(ErrorCode.E005.getMessage());
    }

    @Test
    void 유저가_좌석_결제_성공_후_영수증을_발급받는다() {
        // given
        User save = userJpaRepository.save(new User("user1", 1000));
        Long seatId = 1L;
        String token = "token";
        Concert savedConcert = concertJpaRepository.save(new Concert("콘서트명"));

        LocalDate concertOpenDate = LocalDate.now();
        LocalDateTime startAt = LocalDateTime.now();
        LocalDateTime endAt = startAt.plusMinutes(5L);
        ConcertSchedule savedConcertSchedule = concertScheduleJpaRepository.save(
            new ConcertSchedule(savedConcert, concertOpenDate, startAt, endAt, 50)
        );

        Reservation savedReservation = reservationJpaRepository.save(
            Reservation.builder()
                .concertScheduleId(savedConcertSchedule.getId())
                .seatId(seatId)
                .seatPosition(1)
                .seatAmount(999)
                .build()
        );
        // when
        Receipt receipt = paymentService.pay("token", save.getId(), savedConcertSchedule.getId(), seatId, concertOpenDate);

        entityManager.flush();
        entityManager.clear();

        // then
        Reservation reservation = reservationJpaRepository.findById(savedReservation.getId()).get();
        List<MessageOutbox> tokenMessageOutbox = messageOutboxJpaRepository.findAllByTopicAndEventType(ConcertTopic.token,
            EventType.ACTIVE_TOKEN_DELETE);
        List<MessageOutbox> paymentMessageOutbox = messageOutboxJpaRepository.findAllByTopicAndEventType(ConcertTopic.payment,
            EventType.SEND_PAYMENT_RESULT);

        assertAll(
            () -> assertThat(reservation.getStatus()).isEqualTo(ReservationStatus.RESERVED),
            () -> assertThat(receipt.concertName()).isEqualTo(savedConcert.getTitle()),
            () -> assertThat(receipt.concertOpenDate()).isEqualTo(concertOpenDate),
            () -> assertThat(receipt.seatPosition()).isEqualTo(1),
            () -> assertThat(receipt.purchaseAmount()).isEqualTo(999),
            () -> assertThat(tokenMessageOutbox).hasSize(1),
            () -> assertThat(paymentMessageOutbox).hasSize(1)
        );

    }

    @Test
    void 유저가_좌석_결제_성공_후_마지막_좌석이였을_경우_스케줄의_전체좌석_상태값이_만료로_바뀐다() {
        // given
        User save = userJpaRepository.save(new User("user1", 1000));
        Long seatId = 1L;
        Concert savedConcert = concertJpaRepository.save(new Concert("콘서트명"));

        LocalDate concertOpenDate = LocalDate.now();
        LocalDateTime startAt = LocalDateTime.now();
        LocalDateTime endAt = startAt.plusMinutes(5L);
        ConcertSchedule savedConcertSchedule = concertScheduleJpaRepository.save(
            new ConcertSchedule(savedConcert, concertOpenDate, startAt, endAt, 1)
        );

        reservationJpaRepository.save(
            Reservation.builder()
                .concertScheduleId(savedConcertSchedule.getId())
                .seatId(seatId)
                .seatPosition(1)
                .seatAmount(999)
                .status(ReservationStatus.RESERVED)
                .build()
        );

        // when
        paymentService.pay("token", save.getId(), savedConcertSchedule.getId(), seatId, concertOpenDate);

        entityManager.flush();
        entityManager.clear();

        ConcertSchedule concertSchedule = concertScheduleJpaRepository.findById(savedConcertSchedule.getId()).get();

        // then
        assertAll(
            () -> assertThat(concertSchedule.getStatus()).isEqualTo(TotalSeatStatus.SOLD_OUT)
        );
    }

}
