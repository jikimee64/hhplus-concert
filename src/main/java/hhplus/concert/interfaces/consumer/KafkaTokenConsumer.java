package hhplus.concert.interfaces.consumer;

import hhplus.concert.domain.userqueue.UserQueueService;
import hhplus.concert.infra.producer.kafka.dto.KafkaMessage;
import hhplus.concert.infra.producer.kafka.dto.PublisherTokenMessage;
import java.util.concurrent.CountDownLatch;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaTokenConsumer {

    private static CountDownLatch latch = new CountDownLatch(1);
    private static PublisherTokenMessage tokenMessage;

    private final UserQueueService userQueueService;

    @KafkaListener(topics = "${kafka.topics.concert.token}", containerFactory = "tokenConsumerFactory")
    public void consume(KafkaMessage<PublisherTokenMessage> kafkaMessage, Acknowledgment ack) {
        log.info("kafkaTokenConsumer 수신한 데이터 : {}", kafkaMessage.toString());
        try {
            tokenMessage = kafkaMessage.getPayload();
            userQueueService.deleteActiveToken(tokenMessage.getToken());
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

    public PublisherTokenMessage getTokenMessage() {
        return tokenMessage;
    }
}
