package hhplus.concert.infra.jwt;

import hhplus.concert.domain.UserQueueTokenProvider;

public class JwtUserQueueTokenProviderTest implements UserQueueTokenProvider {

    private String queueToken;
    private Long userId;
    private Long waitingNumber;

    public JwtUserQueueTokenProviderTest(String queueToken) {
        this.queueToken = queueToken;
    }

    public JwtUserQueueTokenProviderTest(Long userId, Long waitingNumber) {
        this.userId = userId;
        this.waitingNumber = waitingNumber;
    }

    @Override
    public String createQueueToken() {
        return queueToken;
    }

}