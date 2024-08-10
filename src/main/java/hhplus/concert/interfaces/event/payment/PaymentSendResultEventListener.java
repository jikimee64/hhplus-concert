package hhplus.concert.interfaces.event.payment;

import hhplus.concert.domain.outbox.MessageOutbox;
import hhplus.concert.domain.outbox.MessageOutboxRepository;
import hhplus.concert.domain.pay.PaymentSendResultEvent;
import hhplus.concert.infra.producer.KafkaProducer;
import hhplus.concert.infra.producer.dto.KafkaPayment;
import hhplus.concert.interfaces.api.support.ApiException;
import hhplus.concert.interfaces.api.support.error.ErrorCode;
import hhplus.concert.interfaces.event.RetryableEventListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.logging.LogLevel;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentSendResultEventListener extends RetryableEventListener<PaymentSendResultEvent> {

    private final KafkaProducer kafkaProducer;
    private final MessageOutboxRepository messageOutboxRepository;

    @Async("threadPoolTaskExecutor")
    @TransactionalEventListener(
        classes = PaymentSendResultEvent.class,
        phase = TransactionPhase.AFTER_COMMIT
    )
    public void handleSendResult(PaymentSendResultEvent event) {
        handleEventWithRetry(event, 1, 1000);
    }

    @Override
    protected void handleEvent(PaymentSendResultEvent event) {
        MessageOutbox messageOutbox = messageOutboxRepository.findById(event.getMessageOutboxId())
            .orElseThrow(
                () -> new ApiException(ErrorCode.E404, LogLevel.INFO, "MessageOutbox not found messageOutboxId = " + event.getMessageOutboxId()));
        try {
            kafkaProducer.producePayment(
                new KafkaPayment(
                    event.getPaymentId(),
                    event.getUserId(),
                    event.getConcertTitle(),
                    event.getConcertOpenDate(),
                    event.getConcertStartAt(),
                    event.getConcertEndAt(),
                    event.getSeatAmount(),
                    event.getSeatPosition(),
                    event.getReservedAt(),
                    event.getPaymentedAt()
                )
            );
            messageOutbox.sendSuccess();
        } catch (Exception e) {
            log.error("Failed to send payment event to kafka", e);
            messageOutbox.sendFail(e.getMessage());
            throw e;
        }

    }
}
