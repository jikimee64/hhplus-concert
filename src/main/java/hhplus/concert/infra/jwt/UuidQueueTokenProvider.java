package hhplus.concert.infra.jwt;

import hhplus.concert.domain.QueueTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UuidQueueTokenProvider implements QueueTokenProvider {

    @Override
    public String createQueueToken() {
        return UUID.randomUUID().toString();
    }
}
