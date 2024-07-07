package hhplus.concert.infra.jwt;

import hhplus.concert.domain.QueueTokenProvider;

public class JwtQueueTokenProviderTest implements QueueTokenProvider {

    private String queueToken;
    private Long userId;
    private Long waitingNumber;

    public JwtQueueTokenProviderTest(String queueToken) {
        this.queueToken = queueToken;
    }

    public JwtQueueTokenProviderTest(Long userId, Long waitingNumber) {
        this.userId = userId;
        this.waitingNumber = waitingNumber;
    }

    @Override
    public String createQueueToken(Long userId, Long waitingNumber) {
        return queueToken;
    }

    @Override
    public Long getUserId(String queueToken) {
        return userId;
    }

    @Override
    public Long getWaitingNumber(String queueToken) {
        return waitingNumber;
    }
}