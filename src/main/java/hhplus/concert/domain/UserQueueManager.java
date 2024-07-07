package hhplus.concert.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserQueueManager {

    private final UserQueueRepository userQueueRepository;
    private final QueueTokenProvider queueTokenProvider;

    public String enterUserQueue(Long concertScheduleId, Long userId) {
        UserQueue savedUserQueue = userQueueRepository.save(concertScheduleId, userId);
        Long waitingNumber = savedUserQueue.getId();
        return queueTokenProvider.createQueueToken(userId, waitingNumber);
    }
}
