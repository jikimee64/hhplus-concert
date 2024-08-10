package hhplus.concert.domain.outbox;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageOutboxService {

    private final MessageOutboxRepository messageOutboxRepository;

    public List<MessageOutbox> findAllBy(String topic, EventType eventType) {
        return messageOutboxRepository.findAllBy(topic, eventType);
    }

}
