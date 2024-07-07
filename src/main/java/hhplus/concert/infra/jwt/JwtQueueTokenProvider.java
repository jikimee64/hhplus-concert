package hhplus.concert.infra.jwt;

import hhplus.concert.domain.QueueTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtQueueTokenProvider implements QueueTokenProvider {

    private final JwtUtil jwtUtil;

    @Override
    public String createQueueToken(Long userId, Long waitingNumber) {
        return jwtUtil.createJwt(userId, waitingNumber);
    }

    @Override
    public Long getUserId(String queueToken) {
        return jwtUtil.getUserId(queueToken);
    }

    @Override
    public Long getWaitingNumber(String queueToken) {
        return jwtUtil.getWaitingNumber(queueToken);
    }
}
