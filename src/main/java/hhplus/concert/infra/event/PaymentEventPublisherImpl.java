package hhplus.concert.infra.event;

import hhplus.concert.domain.pay.PaymentEventPublisher;
import hhplus.concert.domain.pay.PaymentSendResultEvent;
import hhplus.concert.domain.userqueue.ActiveTokenDeleteEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class PaymentEventPublisherImpl implements PaymentEventPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void publishActiveTokenDelete(ActiveTokenDeleteEvent activeTokenDeleteEvent) {
        applicationEventPublisher.publishEvent(activeTokenDeleteEvent);
    }

    @Override
    public void publishPaymentResult(PaymentSendResultEvent reservationEvent) {
        applicationEventPublisher.publishEvent(reservationEvent);
    }
}
