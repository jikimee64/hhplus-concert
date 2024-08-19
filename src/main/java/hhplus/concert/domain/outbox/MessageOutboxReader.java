package hhplus.concert.domain.outbox;

import java.util.List;
import java.util.Optional;

public interface MessageOutboxReader {

    List<MessageOutbox> findAllBy(String topic, EventType eventType);

    Optional<MessageOutbox> findById(Long id);
}
