package hhplus.concert.infra.outbox;

import hhplus.concert.domain.outbox.MessageOutbox;
import hhplus.concert.domain.outbox.MessageOutboxRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MessageOutboxRepositoryImpl implements MessageOutboxRepository {

    private final MessageOutboxJpaRepository messageOutboxJpaRepository;

    @Override
    public MessageOutbox save(MessageOutbox messageOutbox) {
        return messageOutboxJpaRepository.save(messageOutbox);
    }

    @Override
    public Optional<MessageOutbox> findById(Long id) {
        return messageOutboxJpaRepository.findById(id);
    }

}
