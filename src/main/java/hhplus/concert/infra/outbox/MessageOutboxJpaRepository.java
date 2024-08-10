package hhplus.concert.infra.outbox;

import hhplus.concert.domain.outbox.EventType;
import hhplus.concert.domain.outbox.MessageOutbox;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageOutboxJpaRepository extends JpaRepository<MessageOutbox, Long> {

    List<MessageOutbox> findAllByTopicAndEventType(String topic, EventType eventType);

}
