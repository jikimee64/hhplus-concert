package hhplus.concert.domain.userqueue;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@ConfigurationProperties(prefix = "user-queue")
public class UserQueueConstant {
    private final Long maxWaitingNumber;
    private final Long queueTokenExpireTime;

    public UserQueueConstant(Long maxWaitingNumber, Long queueTokenExpireTime) {
        this.maxWaitingNumber = maxWaitingNumber;
        this.queueTokenExpireTime = queueTokenExpireTime;
    }
}
