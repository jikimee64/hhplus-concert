package hhplus.concert.domain.outbox;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import hhplus.concert.infra.producer.dto.KafkaToken;
import org.junit.jupiter.api.Test;

class MessageOutboxTest {

    @Test
    void messageOutbox_생성() {
        // given
        String topic = "topic";
        EventType eventType = EventType.ACTIVE_TOKEN_DELETE;
        String messageKey = "message";
        KafkaToken kafkaToken = new KafkaToken("token");

        // when
        MessageOutbox messageOutbox = MessageOutbox.createMessage(
            topic,
            eventType,
            messageKey,
            kafkaToken
        );

        // then
        assertAll(
            () -> assertThat(messageOutbox.getTopic()).isEqualTo(topic),
            () -> assertThat(messageOutbox.getEventType()).isEqualTo(eventType),
            () -> assertThat(messageOutbox.getMessageKey()).isEqualTo(messageKey),
            () -> assertThat(messageOutbox.getPayload()).isEqualTo(kafkaToken),
            () -> assertThat(messageOutbox.getMessageStatus()).isEqualTo(MessageStatus.PENDING),
            () -> assertThat(messageOutbox.getCreatedAt()).isNotNull(),
            () -> assertThat(messageOutbox.getRetryCount()).isEqualTo(0)
        );
    }

    @Test
    void messageOutbox_전송_성공() {
        // given
        String topic = "topic";
        EventType eventType = EventType.ACTIVE_TOKEN_DELETE;
        String messageKey = "message";
        KafkaToken kafkaToken = new KafkaToken("token");
        MessageOutbox messageOutbox = MessageOutbox.createMessage(
            topic,
            eventType,
            messageKey,
            kafkaToken
        );

        // when
        messageOutbox.sendSuccess();

        // then
        assertAll(
            () -> assertThat(messageOutbox.getMessageStatus()).isEqualTo(MessageStatus.SEND_SUCCESS),
            () -> assertThat(messageOutbox.getUpdatedAt()).isNotNull()
        );

    }

    @Test
    void messageOutbox_전송_실패() {
        // given
        String topic = "topic";
        EventType eventType = EventType.ACTIVE_TOKEN_DELETE;
        String messageKey = "message";
        String errorMessage = "errorMessage";
        KafkaToken kafkaToken = new KafkaToken("token");
        MessageOutbox messageOutbox = MessageOutbox.createMessage(
            topic,
            eventType,
            messageKey,
            kafkaToken
        );

        // when
        messageOutbox.sendFail(errorMessage);

        // then
        assertAll(
            () -> assertThat(messageOutbox.getMessageStatus()).isEqualTo(MessageStatus.SEND_FAIL),
            () -> assertThat(messageOutbox.getErrorMessage()).isEqualTo(errorMessage),
            () -> assertThat(messageOutbox.getUpdatedAt()).isNotNull()
        );

    }

}
