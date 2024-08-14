package hhplus.concert.interfaces.consumer;

import hhplus.concert.infra.producer.kafka.dto.KafkaMessage;
import hhplus.concert.infra.producer.kafka.dto.PublisherPaymentMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class KafkaPaymentConsumer {

    @KafkaListener(topics = "${kafka.topics.concert.payment}", containerFactory = "paymentConsumerFactory")
    public void consume(KafkaMessage<PublisherPaymentMessage> kafkaTemplate, Acknowledgment ack) {
        log.info("kafkaPaymentConsumer 수신한 데이터 : {}", kafkaTemplate.toString());
        try {
            PublisherPaymentMessage payloadMessage = kafkaTemplate.getPayload();

            ack.acknowledge();
        } catch (Exception e) {
            log.error("consume Error - Exception : {}", e.getMessage());
            throw e;
        }
    }

}
