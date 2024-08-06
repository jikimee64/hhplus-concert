package hhplus.concert.domain.pay;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import hhplus.concert.domain.concert.Concert;
import hhplus.concert.domain.concert.ConcertSchedule;
import hhplus.concert.domain.concert.Reservation;
import hhplus.concert.domain.pay.dto.Receipt;
import hhplus.concert.domain.user.User;
import hhplus.concert.domain.userqueue.ActiveTokenDeleteEvent;
import hhplus.concert.infra.persistence.ConcertJpaRepository;
import hhplus.concert.infra.persistence.ConcertScheduleJpaRepository;
import hhplus.concert.infra.persistence.ReservationJpaRepository;
import hhplus.concert.infra.persistence.UserJpaRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.event.ApplicationEvents;
import org.springframework.test.context.event.RecordApplicationEvents;

@SpringBootTest
@RecordApplicationEvents
class PaymentServiceTest {

    @Autowired
    private UserJpaRepository userJpaRepository;

    @Autowired
    private ReservationJpaRepository reservationJpaRepository;

    @Autowired
    private ConcertJpaRepository concertJpaRepository;

    @Autowired
    private ConcertScheduleJpaRepository concertScheduleJpaRepository;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private ApplicationEvents events;

    @Test
    void 결제_스프링_이벤트_호출_테스트() {
        // given
        User savedUser = userJpaRepository.save(new User("user1", 1000));
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
        Receipt receipt = paymentService.pay("token", savedUser.getId(), savedConcertSchedule.getId(), seatId, concertOpenDate);

        // then
        assertThat(events.stream(ActiveTokenDeleteEvent.class))
            .hasSize(1)
            .anySatisfy(event -> {
                assertAll(
                    () -> assertThat(event.getToken()).isEqualTo(token)
                );
            });

        assertThat(events.stream(PaymentSendResultEvent.class))
            .hasSize(1)
            .anySatisfy(event -> {
                assertAll(
                    () -> assertThat(event.getUserId()).isEqualTo(savedUser.getId()),
                    () -> assertThat(event.getConcertTitle()).isEqualTo(savedConcert.getTitle()),
                    () -> assertThat(event.getConcertOpenDate()).isEqualTo(savedConcertSchedule.getOpenDate()),
                    () -> assertThat(event.getConcertStartAt()).isEqualTo(savedConcertSchedule.getStartAt()),
                    () -> assertThat(event.getConcertEndAt()).isEqualTo(savedConcertSchedule.getEndAt()),
                    () -> assertThat(event.getSeatAmount()).isEqualTo(999),
                    () -> assertThat(event.getSeatPosition()).isEqualTo(1),
                    () -> assertThat(event.getReservedAt()).isEqualTo(savedReservation.getReservedAt()),
                    () -> assertThat(event.getPaymentedAt()).isEqualTo(receipt.purchaseDate()
                    ));
            });
    }

}
