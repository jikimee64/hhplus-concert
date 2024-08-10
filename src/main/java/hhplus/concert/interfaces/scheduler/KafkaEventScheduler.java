package hhplus.concert.interfaces.scheduler;

import com.fasterxml.jackson.databind.ObjectMapper;
import hhplus.concert.domain.outbox.EventType;
import hhplus.concert.domain.outbox.MessageOutbox;
import hhplus.concert.domain.outbox.MessageOutboxService;
import hhplus.concert.domain.outbox.MessageStatus;
import hhplus.concert.domain.pay.PaymentEventPublisher;
import hhplus.concert.domain.pay.PaymentSendResultEvent;
import hhplus.concert.domain.userqueue.ActiveTokenDeleteEvent;
import hhplus.concert.support.constant.ConcertTopic;
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Consumer;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaEventScheduler {

    private final MessageOutboxService messageOutboxService;
    private final PaymentEventPublisher paymentEventPublisher;
    private final ObjectMapper objectMapper;

    @Scheduled(fixedDelay = 10000)
    public void publishRetryToken() {
        processRetryMessages(
            ConcertTopic.token,
            EventType.ACTIVE_TOKEN_DELETE,
            message -> paymentEventPublisher.publishActiveTokenDelete(
                message.getPayloadAs(objectMapper, ActiveTokenDeleteEvent.class)
            )
        );
    }

    @Scheduled(fixedDelay = 10000)
    public void publishRetryPayment() {
        processRetryMessages(
            ConcertTopic.payment,
            EventType.SEND_PAYMENT_RESULT,
            message -> paymentEventPublisher.publishPaymentResult(
                message.getPayloadAs(objectMapper, PaymentSendResultEvent.class)
            )
        );
    }

    private void processRetryMessages(String topic, EventType eventType, Consumer<MessageOutbox> messageProcessor) {
        List<MessageOutbox> messageOutboxes = messageOutboxService.findAllBy(topic, eventType);
        LocalDateTime tenMinutesAgo = LocalDateTime.now().minusMinutes(10);

        for (MessageOutbox messageOutbox : messageOutboxes) {
            if (shouldRetryMessage(messageOutbox, tenMinutesAgo)) {
                messageProcessor.accept(messageOutbox);
            }
        }
    }

    private boolean shouldRetryMessage(MessageOutbox messageOutbox, LocalDateTime tenMinutesAgo) {
        return messageOutbox.getMessageStatus() != MessageStatus.SEND_SUCCESS &&
            messageOutbox.getCreatedAt().isBefore(tenMinutesAgo);
    }
}