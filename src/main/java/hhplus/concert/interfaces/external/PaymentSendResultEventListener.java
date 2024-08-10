package hhplus.concert.interfaces.external;

import hhplus.concert.application.external.PaymentSendResultEventFacade;
import hhplus.concert.domain.pay.PaymentSendResultEvent;
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
public class PaymentSendResultEventListener extends RetryableEventListener<PaymentSendResultEvent> {

    private final PaymentSendResultEventFacade paymentSendResultEventFacade;

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
        paymentSendResultEventFacade.sendPaymentResult(event);
    }
}
