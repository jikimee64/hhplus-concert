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
    public String createQueueToken() {
        return queueToken;
    }

}