package hhplus.concert.interfaces.scheduler;

import hhplus.concert.domain.userqueue.UserQueueService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserQueueScheduler {

    private final UserQueueService userQueueService;

    @Scheduled(fixedDelay = 5000, initialDelay = 1800_000)
    public void checkTokenExpire() {
        userQueueService.updateExpireConditionToken();
    }

    @Scheduled(fixedDelay = 5000)
    public void enteringUserQueue() {
        userQueueService.periodicallyEnterUserQueue();
    }
}
