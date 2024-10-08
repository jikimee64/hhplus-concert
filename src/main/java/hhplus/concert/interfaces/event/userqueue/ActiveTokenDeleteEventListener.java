package hhplus.concert.interfaces.event.userqueue;

import hhplus.concert.domain.outbox.MessageOutbox;
import hhplus.concert.domain.outbox.MessageOutboxReader;
import hhplus.concert.domain.userqueue.ActiveTokenDeleteEvent;
import hhplus.concert.domain.userqueue.ActiveTokenPublisher;
import hhplus.concert.infra.producer.kafka.dto.PublisherTokenMessage;
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
public class ActiveTokenDeleteEventListener extends RetryableEventListener<ActiveTokenDeleteEvent> {

    private final ActiveTokenPublisher activeTokenPublisher;
    private final MessageOutboxReader messageOutboxReader;

    @Async("threadPoolTaskExecutor")
    @TransactionalEventListener(
        classes = ActiveTokenDeleteEvent.class,
        phase = TransactionPhase.AFTER_COMMIT
    )
    public void handleActiveTokenDelete(ActiveTokenDeleteEvent event) {
        handleEventWithRetry(event, 1, 1000);
    }

    @Override
    protected void handleEvent(ActiveTokenDeleteEvent event) {
        MessageOutbox messageOutbox = messageOutboxReader.findById(event.getMessageOutboxId())
            .orElseThrow(
                () -> new ApiException(ErrorCode.E404, LogLevel.INFO, "MessageOutbox not found messageOutboxId = " + event.getMessageOutboxId()));
        try {
            messageOutbox.sendSuccess();
            activeTokenPublisher.publishActiveToken(
                new PublisherTokenMessage(event.getToken()), System.currentTimeMillis()
            );
        } catch (Exception e) {
            log.error("Failed to send active token event to kafka", e);
            messageOutbox.sendFail(e.getMessage());
            throw e;
        }
    }

}
