package hhplus.concert.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserQueueTokenChecker {

    private final UserQueueRepository userQueueRepository;

    public Integer updateExpireConditionToken() {
        return userQueueRepository.updateExpireConditionToken();
    }
}
