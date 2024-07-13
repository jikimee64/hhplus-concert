package hhplus.concert.domain.userqueue;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@ConfigurationProperties(prefix = "user-queue")
public class UserQueueConstant {
    private final Integer maxWaitingNumber;
    private final Integer queueTokenExpireTime;

    public UserQueueConstant(Integer maxWaitingNumber, Integer queueTokenExpireTime) {
        this.maxWaitingNumber = maxWaitingNumber;
        this.queueTokenExpireTime = queueTokenExpireTime;
    }
}
