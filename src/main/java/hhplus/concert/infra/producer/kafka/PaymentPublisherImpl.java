package hhplus.concert.infra.producer.kafka;

import hhplus.concert.domain.pay.PaymentPublisher;
import hhplus.concert.infra.producer.kafka.dto.KafkaMessage;
import hhplus.concert.infra.producer.kafka.dto.PublisherPaymentMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentPublisherImpl implements PaymentPublisher {

    private final KafkaProducer kafkaProducer;

    @Override
    public void publishPayment(PublisherPaymentMessage message, Long publishTimestamp) {
        kafkaProducer.producePayment(
            new KafkaMessage(
                System.currentTimeMillis(),
                publishTimestamp,
                message
            )
        );
    }
}
