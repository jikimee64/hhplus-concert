package hhplus.concert.infra.producer.kafka;

import hhplus.concert.domain.userqueue.ActiveTokenPublisher;
import hhplus.concert.infra.producer.kafka.dto.KafkaMessage;
import hhplus.concert.infra.producer.kafka.dto.PublisherTokenMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ActiveTokenPublisherImpl implements ActiveTokenPublisher {

    private final KafkaProducer kafkaProducer;

    @Override
    public void publishActiveToken(PublisherTokenMessage message, Long publishTimestamp) {
        kafkaProducer.produceActiveToken(
            new KafkaMessage(
                System.currentTimeMillis(),
                publishTimestamp,
                message
            )
        );
    }

}
