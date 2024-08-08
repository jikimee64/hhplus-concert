package hhplus.concert.domain.pay;

import hhplus.concert.domain.userqueue.ActiveTokenDeleteEvent;

public interface PaymentEventPublisher {

    void publishActiveTokenDelete(ActiveTokenDeleteEvent activeTokenDeleteEvent);

    void publishPaymentResult(PaymentSendResultEvent reservationEvent);
}
