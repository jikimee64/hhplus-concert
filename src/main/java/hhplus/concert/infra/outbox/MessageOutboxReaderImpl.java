package hhplus.concert.infra.outbox;

import hhplus.concert.domain.outbox.EventType;
import hhplus.concert.domain.outbox.MessageOutbox;
import hhplus.concert.domain.outbox.MessageOutboxReader;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MessageOutboxReaderImpl implements MessageOutboxReader {

    private final MessageOutboxJpaRepository messageOutboxJpaRepository;

    @Override
    public List<MessageOutbox> findAllBy(String topic, EventType eventType) {
        return messageOutboxJpaRepository.findAllByTopicAndEventType(topic, eventType);
    }

    @Override
    public Optional<MessageOutbox> findById(Long id) {
        return messageOutboxJpaRepository.findById(id);
    }

}
