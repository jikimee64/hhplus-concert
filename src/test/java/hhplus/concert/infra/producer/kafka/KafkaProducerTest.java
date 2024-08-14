package hhplus.concert.infra.producer.kafka;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import hhplus.concert.infra.producer.kafka.dto.KafkaMessage;
import hhplus.concert.infra.producer.kafka.dto.PublisherTokenMessage;
import hhplus.concert.interfaces.consumer.KafkaTokenConsumer;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest
@EmbeddedKafka(partitions = 1,
    brokerProperties = {"listeners=PLAINTEXT://localhost:9092"},
    ports = {9092}
)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class KafkaProducerTest {

    @Autowired
    private KafkaTokenConsumer consumer;

    @Autowired
    private KafkaProducer producer;

    @Test
    public void 카프카_메시지_전송_및_수신_테스트() throws Exception {

        String token = "token";

        producer.produceActiveToken(
            new KafkaMessage(
                System.currentTimeMillis(),
                System.currentTimeMillis(),
                new PublisherTokenMessage(token)
            )
        );

        consumer.getLatch().await(15, TimeUnit.SECONDS);
        assertThat(consumer.getTokenMessage().getToken()).isEqualTo(token);
    }

}
