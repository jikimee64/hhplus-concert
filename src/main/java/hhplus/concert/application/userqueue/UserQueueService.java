package hhplus.concert.application.userqueue;

import hhplus.concert.domain.userqueue.UserQueueManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserQueueService {

    private final UserQueueManager userQueueManager;

    public String selectToken(Long concertScheduleId, Long userId) {
        return userQueueManager.enterUserQueue(concertScheduleId, userId);
    }
    
    public Integer selectWaitingNumber(String token, Long concertScheduleId) {
        return userQueueManager.selectWaitingNumber(token, concertScheduleId);

    }
}
