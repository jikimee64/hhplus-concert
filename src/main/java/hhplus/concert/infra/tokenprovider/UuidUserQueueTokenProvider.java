package hhplus.concert.infra.tokenprovider;

import hhplus.concert.domain.userqueue.UserQueueTokenProvider;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UuidUserQueueTokenProvider implements UserQueueTokenProvider {

    @Override
    public String createQueueToken() {
        return UUID.randomUUID().toString();
    }
}
