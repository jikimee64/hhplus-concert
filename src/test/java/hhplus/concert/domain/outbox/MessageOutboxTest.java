package hhplus.concert.domain.outbox;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import hhplus.concert.domain.userqueue.ActiveTokenDeleteEvent;
import hhplus.concert.infra.producer.kafka.dto.PublisherTokenMessage;
import org.junit.jupiter.api.Test;

class MessageOutboxTest {

    @Test
    void messageOutbox_생성() {
        // given
        String topic = "topic";
        EventType eventType = EventType.ACTIVE_TOKEN_DELETE;
        String messageKey = "message";
        PublisherTokenMessage publisherTokenMessage = new PublisherTokenMessage("token");

        // when
        MessageOutbox messageOutbox = MessageOutbox.createMessage(
            topic,
            eventType,
            messageKey,
            publisherTokenMessage
        );

        // then
        assertAll(
            () -> assertThat(messageOutbox.getTopic()).isEqualTo(topic),
            () -> assertThat(messageOutbox.getEventType()).isEqualTo(eventType),
            () -> assertThat(messageOutbox.getMessageKey()).isEqualTo(messageKey),
            () -> assertThat(messageOutbox.getPayload()).isEqualTo(publisherTokenMessage),
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
        PublisherTokenMessage publisherTokenMessage = new PublisherTokenMessage("token");
        MessageOutbox messageOutbox = MessageOutbox.createMessage(
            topic,
            eventType,
            messageKey,
            publisherTokenMessage
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
        PublisherTokenMessage publisherTokenMessage = new PublisherTokenMessage("token");
        MessageOutbox messageOutbox = MessageOutbox.createMessage(
            topic,
            eventType,
            messageKey,
            publisherTokenMessage
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

    @Test
    void messageOutbox_payload_조회() {
        ObjectMapper objectMapper = JsonMapper.builder()
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
            .build()
            .registerModule(new JavaTimeModule());

        String topic = "topic";
        EventType eventType = EventType.ACTIVE_TOKEN_DELETE;
        String messageKey = "message";
        String token = "token";
        PublisherTokenMessage publisherTokenMessage = new PublisherTokenMessage(token);
        MessageOutbox messageOutbox = MessageOutbox.createMessage(
            topic,
            eventType,
            messageKey,
            publisherTokenMessage
        );

        ActiveTokenDeleteEvent event = messageOutbox.getPayloadAs(objectMapper, ActiveTokenDeleteEvent.class);
        assertThat(event.getToken()).isEqualTo("token");
    }

}
