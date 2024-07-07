package hhplus.concert.infra.jwt;

import hhplus.concert.domain.QueueTokenProvider;

public class JwtQueueTokenProviderTest implements QueueTokenProvider {

    private final String queueToken;

    public JwtQueueTokenProviderTest(String queueToken) {
        this.queueToken = queueToken;
    }

    @Override
    public String createQueueToken(Long userId, Long waitingNumber) {
        return queueToken;
    }
}