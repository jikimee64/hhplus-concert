package hhplus.concert.interfaces.scheduler;

import hhplus.concert.domain.userqueue.UserQueueTokenChecker;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserQueueScheduler {

    private final UserQueueTokenChecker userQueueTokenChecker;

    /**
     * 1800초(30분) 이후 5초 마다 토큰 만료 여부 조건일 경우 토큰 상태값을 업데이트 한다.
     * 작업을 마친 시점 기준으로 5초마다 실행된다.
     */
    @Scheduled(fixedDelay = 5000, initialDelay = 1800_000)
    public void checkTokenExpire() {
        userQueueTokenChecker.updateExpireConditionToken();
    }
}
