package hhplus.concert.infra.producer.kafka;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import hhplus.concert.infra.producer.kafka.dto.KafkaMessage;
import hhplus.concert.infra.producer.kafka.dto.PublisherPaymentMessage;
import hhplus.concert.infra.producer.kafka.dto.PublisherTokenMessage;
import hhplus.concert.interfaces.consumer.KafkaPaymentConsumer;
import hhplus.concert.interfaces.consumer.KafkaTokenConsumer;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
    private KafkaTokenConsumer tokenConsumer;

    @Autowired
    private KafkaPaymentConsumer paymentConsumer;

    @Autowired
    private KafkaProducer producer;

    @Test
    public void 카프카_토큰_이벤트_발행_메시지_전송_및_수신_테스트() throws Exception {

        String token = "token";

        producer.produceActiveToken(
            new KafkaMessage(
                System.currentTimeMillis(),
                System.currentTimeMillis(),
                new PublisherTokenMessage(token)
            )
        );

        tokenConsumer.getLatch().await(15, TimeUnit.SECONDS);
        PublisherTokenMessage tokenMessage = tokenConsumer.getTokenMessage();
        assertThat(tokenMessage.getToken()).isEqualTo(token);
    }

    @Test
    public void 카프카_결제_이벤트_발행_메시지_전송_및_수신_테스트() throws Exception {

        producer.producePayment(
            new KafkaMessage(
                System.currentTimeMillis(),
                System.currentTimeMillis(),
                new PublisherPaymentMessage(
                    1L,
                    2L,
                    "concertTitle",
                    LocalDate.now(),
                    LocalDateTime.now(),
                    LocalDateTime.now(),
                    1,
                    1,
                    LocalDateTime.now(),
                    LocalDateTime.now()
                )
            )
        );

        paymentConsumer.getLatch().await(15, TimeUnit.SECONDS);
        PublisherPaymentMessage paymentMessage = paymentConsumer.getPaymentMessage();
        assertThat(paymentMessage.getId()).isEqualTo(1L);
        assertThat(paymentMessage.getUserId()).isEqualTo(2L);
    }

}
