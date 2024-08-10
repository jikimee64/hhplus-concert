package hhplus.concert.infra.producer;

import hhplus.concert.infra.producer.dto.KafkaPayment;
import hhplus.concert.infra.producer.dto.KafkaToken;
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

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void produceActiveToken(KafkaToken kafkaToken) {
        try {
            Message<KafkaToken> message = MessageBuilder
                .withPayload(kafkaToken)
                .setHeader(KafkaHeaders.TOPIC, ConcertTopic.payment)
                .setHeader(KafkaHeaders.KEY, kafkaToken.getToken())
                .build();

            kafkaTemplate.send(message);
        } catch (Exception e) {
            log.error(">>> [ALARM] Kafka produceActiveToken Send Error= {}" + e);
        }
    }

    public void producePayment(KafkaPayment kafkaPayment) {
        try {
            Message<KafkaPayment> message = MessageBuilder
                .withPayload(kafkaPayment)
                .setHeader(KafkaHeaders.TOPIC, ConcertTopic.token)
                .setHeader(KafkaHeaders.KEY, kafkaPayment.getKey())
                .build();

            kafkaTemplate.send(message);
        } catch (Exception e) {
            log.error(">>> [ALARM] Kafka producePayment Send Error= {}" + e);
        }
    }

}
