package hhplus.concert.interfaces.scheduler;

import hhplus.concert.domain.userqueue.UserQueueManager;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserQueueScheduler {

    private final UserQueueManager userQueueManager;

    @Scheduled(fixedDelay = 5000, initialDelay = 1800_000)
    public void checkTokenExpire() {
        userQueueManager.updateExpireConditionToken();
    }

    @Scheduled(fixedDelay = 5000)
    public void enteringUserQueue() {
        userQueueManager.periodicallyEnterUserQueue();
    }
}
