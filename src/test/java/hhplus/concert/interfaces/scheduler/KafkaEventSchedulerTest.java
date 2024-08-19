package hhplus.concert.interfaces.scheduler;

import static hhplus.concert.support.constant.ConcertTopic.payment;
import static hhplus.concert.support.constant.ConcertTopic.token;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import hhplus.concert.domain.outbox.EventType;
import hhplus.concert.domain.outbox.MessageOutbox;
import hhplus.concert.domain.outbox.MessageStatus;
import hhplus.concert.domain.pay.PaymentSendResultEvent;
import hhplus.concert.domain.userqueue.ActiveTokenDeleteEvent;
import hhplus.concert.infra.outbox.MessageOutboxJpaRepository;
import java.time.LocalDateTime;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.event.ApplicationEvents;
import org.springframework.test.context.event.RecordApplicationEvents;

@SpringBootTest
@ActiveProfiles("test")
@RecordApplicationEvents
class KafkaEventSchedulerTest {

    @Autowired
    private KafkaEventScheduler kafkaEventScheduler;

    @Autowired
    private MessageOutboxJpaRepository messageOutboxJpaRepository;

    @Autowired
    private ApplicationEvents events;

    @ParameterizedTest
    @EnumSource(value = MessageStatus.class, names = {"SEND_FAIL", "PENDING"})
    void 카프카_결제_이벤트_발행_실패_재시도(MessageStatus messageStatus) {
        // given
        LocalDateTime createdAt = LocalDateTime.now().minusMinutes(10).minusSeconds(1);
        PaymentSendResultEvent paymentSendResultEvent = new PaymentSendResultEvent(
            1L,
            1L,
            "concertTitle",
            LocalDateTime.now().toLocalDate(),
            LocalDateTime.now(),
            LocalDateTime.now(),
            1,
            1,
            LocalDateTime.now(),
            LocalDateTime.now()
        );
        paymentSendResultEvent.setMessageOutboxId(1L);
        messageOutboxJpaRepository.save(
            MessageOutbox.builder()
                .topic(payment) // 토픽
                .eventType(EventType.SEND_PAYMENT_RESULT)
                .messageStatus(messageStatus)
                .payload(paymentSendResultEvent)
                .createdAt(createdAt)
                .build()
        );

        // when
        kafkaEventScheduler.publishRetryPayment();

        // then
        assertThat(events.stream(PaymentSendResultEvent.class))
            .hasSizeGreaterThan(0)
            .anySatisfy(event -> {
                assertAll(
                    () -> assertThat(event.getPaymentId()).isEqualTo(1L),
                    () -> assertThat(event.getUserId()).isEqualTo(1L),
                    () -> assertThat(event.getMessageOutboxId()).isEqualTo(1L)
                );
            });
    }

    @ParameterizedTest
    @EnumSource(value = MessageStatus.class, names = {"SEND_FAIL", "PENDING"})
    void 카프카_토큰_메시지_발행_실패_재시도(MessageStatus messageStatus) {
        // given
        LocalDateTime createdAt = LocalDateTime.now().minusMinutes(10).minusSeconds(1);
        ActiveTokenDeleteEvent activeTokenDeleteEvent = new ActiveTokenDeleteEvent("token");
        activeTokenDeleteEvent.setMessageOutboxId(1L);
        messageOutboxJpaRepository.save(
            MessageOutbox.builder()
                .topic(token) // 토픽
                .eventType(EventType.ACTIVE_TOKEN_DELETE)
                .messageStatus(messageStatus)
                .payload(activeTokenDeleteEvent)
                .createdAt(createdAt)
                .build()
        );

        // when
        kafkaEventScheduler.publishRetryToken();

        // then
        assertThat(events.stream(ActiveTokenDeleteEvent.class))
            .hasSizeGreaterThan(0)
            .anySatisfy(event -> {
                assertAll(
                    () -> assertThat(event.getToken()).isEqualTo("token"),
                    () -> assertThat(event.getMessageOutboxId()).isEqualTo(1L)
                );
            });
    }

}
