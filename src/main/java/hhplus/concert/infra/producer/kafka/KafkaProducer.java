package hhplus.concert.infra.producer.kafka;

import hhplus.concert.infra.producer.kafka.dto.KafkaMessage;
import hhplus.concert.support.constant.ConcertTopic;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaProducer {

    private final KafkaTemplate<String, KafkaMessage> kafkaTemplate;

    public void produceActiveToken(KafkaMessage kafkaMessage) {
        try {
            Message<KafkaMessage> message = MessageBuilder
                .withPayload(kafkaMessage)
                .setHeader(KafkaHeaders.TOPIC, ConcertTopic.token)
                .setHeader(KafkaHeaders.KEY, kafkaMessage.getEventKey())
                .build();

            kafkaTemplate.send(message);
        } catch (Exception e) {
            log.error(">>> [ALARM] Kafka produceActiveToken Send Error= {}" + e);
            throw e;
        }
    }

    public void producePayment(KafkaMessage kafkaMessage) {
        try {
            Message<KafkaMessage> message = MessageBuilder
                .withPayload(kafkaMessage)
                .setHeader(KafkaHeaders.TOPIC, ConcertTopic.payment)
                .setHeader(KafkaHeaders.KEY, kafkaMessage.getEventKey())
                .build();

            kafkaTemplate.send(message);
        } catch (Exception e) {
            log.error(">>> [ALARM] Kafka producePayment Send Error= {}" + e);
            throw e;
        }
    }

}
