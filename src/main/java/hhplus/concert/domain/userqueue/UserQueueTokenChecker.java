package hhplus.concert.domain.userqueue;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
@RequiredArgsConstructor
public class UserQueueTokenChecker {

    private final UserQueueRepository userQueueRepository;

    public Integer updateExpireConditionToken() {
        return userQueueRepository.updateExpireConditionToken();
    }
}
