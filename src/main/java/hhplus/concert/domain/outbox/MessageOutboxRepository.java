package hhplus.concert.domain.outbox;

import java.util.Optional;

public interface MessageOutboxRepository {

    MessageOutbox save(MessageOutbox messageOutbox);

    Optional<MessageOutbox> findById(Long id);
}
