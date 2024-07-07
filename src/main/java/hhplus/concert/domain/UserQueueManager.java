package hhplus.concert.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UserQueueManager {

    private final UserQueueRepository userQueueRepository;
    private final QueueTokenProvider queueTokenProvider;

    @Transactional
    public String enterUserQueue(Long concertScheduleId, Long userId) {
        UserQueue savedUserQueue = userQueueRepository.save(concertScheduleId, userId);
        Long waitingNumber = savedUserQueue.getId();
        return queueTokenProvider.createQueueToken(userId, waitingNumber);
    }

    @Transactional(readOnly = true)
    public Long selectWaitingNumber(Long concertScheduleId, Long userId, Long userWaitingNumber) {
        List<UserQueue>  userQueues = userQueueRepository.findStatusIsProgressBy(concertScheduleId);
        return 1L;
    }
}
