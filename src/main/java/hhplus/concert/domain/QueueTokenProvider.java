package hhplus.concert.domain;

public interface QueueTokenProvider {
    String createQueueToken(Long userId, Long waitingNumber);
}
