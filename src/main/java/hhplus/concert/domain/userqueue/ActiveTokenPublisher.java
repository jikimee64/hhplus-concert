package hhplus.concert.domain.userqueue;

import hhplus.concert.infra.producer.kafka.dto.PublisherTokenMessage;

public interface ActiveTokenPublisher {

    void publishActiveToken(PublisherTokenMessage event, Long publishTimestamp);

}
