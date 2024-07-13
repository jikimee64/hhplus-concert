package hhplus.concert.infra.tokenprovider;

import hhplus.concert.domain.userqueue.UserQueueTokenProvider;

public class UuidUserQueueTokenProviderTest implements UserQueueTokenProvider {

    private String queueToken;
    private Long userId;
    private Long waitingNumber;

    public UuidUserQueueTokenProviderTest(String queueToken) {
        this.queueToken = queueToken;
    }

    public UuidUserQueueTokenProviderTest(Long userId, Long waitingNumber) {
        this.userId = userId;
        this.waitingNumber = waitingNumber;
    }

    @Override
    public String createQueueToken() {
        return queueToken;
    }

}