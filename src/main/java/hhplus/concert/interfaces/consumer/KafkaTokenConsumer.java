package hhplus.concert.interfaces.consumer;

import hhplus.concert.infra.producer.dto.KafkaToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class KafkaTokenConsumer {

    @KafkaListener(topics = "${kafka.topics.concert.token}", containerFactory = "paymentConsumerFactory")
    public void consume(KafkaToken kafkaToken, Acknowledgment ack) {
        log.info("kafkaTokenConsumer 수신한 데이터 : {}", kafkaToken.toString());
        try {
            ack.acknowledge();
        } catch (Exception e) {
            log.error("consume Error - Exception : {}", e.getMessage());
        }
    }

}
