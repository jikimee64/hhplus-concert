package hhplus.concert.domain.outbox;

public interface MessageOutboxWriter {

    MessageOutbox save(MessageOutbox messageOutbox);
}
