package hhplus.concert.interfaces.event.userqueue;

import hhplus.concert.domain.userqueue.ActiveTokenDeleteEvent;
import hhplus.concert.infra.producer.KafkaProducer;
import hhplus.concert.infra.producer.dto.KafkaToken;
import hhplus.concert.interfaces.event.RetryableEventListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class ActiveTokenDeleteEventListener extends RetryableEventListener<ActiveTokenDeleteEvent> {

    private final KafkaProducer kafkaProducer;

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
        kafkaProducer.produceActiveToken(
            new KafkaToken(event.getToken())
        );
    }

}
