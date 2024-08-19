package hhplus.concert.infra.outbox;

import hhplus.concert.domain.outbox.MessageOutbox;
import hhplus.concert.domain.outbox.MessageOutboxWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MessageOutboxWriterImpl implements MessageOutboxWriter {

    private final MessageOutboxJpaRepository messageOutboxJpaRepository;

    @Override
    public MessageOutbox save(MessageOutbox messageOutbox) {
        return messageOutboxJpaRepository.save(messageOutbox);
    }

}
