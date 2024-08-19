package hhplus.concert.domain.outbox;

import com.fasterxml.jackson.databind.ObjectMapper;
import hhplus.concert.support.converter.JsonConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "message_outbox")
public class MessageOutbox {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String topic;

    @Enumerated(EnumType.STRING)
    private EventType eventType;

    private String messageKey;

    @Convert(converter = JsonConverter.class)
    @Column(nullable = false, columnDefinition = "TEXT")
    private Object payload;

    @Enumerated(EnumType.STRING)
    private MessageStatus messageStatus = MessageStatus.PENDING;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime updatedAt;

    @Builder.Default
    private Integer retryCount = 0;

    @Column(columnDefinition = "LONGTEXT", nullable = true)
    private String errorMessage;

    public static MessageOutbox createMessage(String topic, EventType eventType, String messageKey, Object payload) {
        return MessageOutbox.builder()
            .topic(topic)
            .eventType(eventType)
            .messageKey(messageKey)
            .payload(payload)
            .messageStatus(MessageStatus.PENDING)
            .createdAt(LocalDateTime.now())
            .retryCount(0)
            .build();
    }

    public void sendSuccess() {
        LocalDateTime now = LocalDateTime.now();
        this.messageStatus = MessageStatus.SEND_SUCCESS;
        this.updatedAt = now;
    }

    public void sendFail(String errorMessage) {
        LocalDateTime now = LocalDateTime.now();
        this.messageStatus = MessageStatus.SEND_FAIL;
        this.updatedAt = now;
        this.errorMessage = errorMessage;
    }

    public <T> T getPayloadAs(ObjectMapper objectMapper, Class<T> clazz) {
        return objectMapper.convertValue(this.payload, clazz);
    }

}
