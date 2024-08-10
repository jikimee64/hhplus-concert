package hhplus.concert.domain.outbox;

import java.util.List;
import java.util.Optional;

public interface MessageOutboxRepository {

    MessageOutbox save(MessageOutbox messageOutbox);

    Optional<MessageOutbox> findById(Long id);

    List<MessageOutbox> findAllBy(String topic, EventType eventType);
}
