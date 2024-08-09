package hhplus.concert.kafka;

import hhplus.concert.support.constant.ConcertTopic;
import hhplus.concert.support.dto.ProducerDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.test.context.ActiveProfiles;

@Slf4j
@SpringBootTest
@ActiveProfiles("test")
public class KafkaProducerTest {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Test
    @Order(1)
    void 카프카_전송_테스트() {
        try {
            ProducerDto producerDto = new ProducerDto("1", "test message");

            Message<ProducerDto> message = MessageBuilder
                .withPayload(producerDto)
                .setHeader(KafkaHeaders.TOPIC, ConcertTopic.payment)
                .setHeader(KafkaHeaders.KEY, producerDto.getMessageKey())
                .build();

            kafkaTemplate.send(message);

        } catch (Exception e) {
            log.error(">>> [ALARM] Kafka Send Error= {}" + e);
        }
    }
}
