package hhplus.concert.infra.producer.kafka.dto;

import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class KafkaMessage<T> {

    private Long publishTimestamp;
    private Long createTimestamp;
    private String eventKey;
    private T payload;

    public KafkaMessage(Long publishTimestamp, Long createTimestamp, T payload) {
        this.publishTimestamp = publishTimestamp;
        this.createTimestamp = createTimestamp;
        this.eventKey = UUID.randomUUID().toString();
        this.payload = payload;
    }
}
