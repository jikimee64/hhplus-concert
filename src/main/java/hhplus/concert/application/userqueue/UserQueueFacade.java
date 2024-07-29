package hhplus.concert.application.userqueue;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserQueueFacade {

    private final hhplus.concert.domain.userqueue.UserQueueService userQueueService;

    public String selectToken(Long concertScheduleId, Long userId) {
        return userQueueService.enterUserQueue(concertScheduleId, userId);
    }
    
    public Integer selectWaitingNumber(String token, Long concertScheduleId) {
        return userQueueService.selectWaitingNumber(token, concertScheduleId);

    }
}
