package hhplus.concert.domain.pay;

import hhplus.concert.infra.producer.kafka.dto.PublisherPaymentMessage;

public interface PaymentPublisher {

    void publishPayment(PublisherPaymentMessage message, Long publishTimestamp);
}
