package hhplus.concert.interfaces.consumer;

import hhplus.concert.infra.producer.kafka.dto.KafkaMessage;
import hhplus.concert.infra.producer.kafka.dto.PublisherPaymentMessage;
import java.util.concurrent.CountDownLatch;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class KafkaPaymentConsumer {

    private static CountDownLatch latch = new CountDownLatch(1);
    private static PublisherPaymentMessage paymentMessage;

    @KafkaListener(topics = "${kafka.topics.concert.payment}", containerFactory = "paymentConsumerFactory")
    public void consume(KafkaMessage<PublisherPaymentMessage> kafkaTemplate, Acknowledgment ack) {
        log.info("kafkaPaymentConsumer 수신한 데이터 : {}", kafkaTemplate.toString());
        try {
            paymentMessage = kafkaTemplate.getPayload();

            ack.acknowledge();
        } catch (Exception e) {
            log.error("consume Error - Exception : {}", e.getMessage());
            throw e;
        }
        latch.countDown();
    }

    public void resetLatch() {
        latch = new CountDownLatch(1);
    }

    public CountDownLatch getLatch() {
        return latch;
    }

    public PublisherPaymentMessage getPaymentMessage() {
        return paymentMessage;
    }

}
