package hhplus.concert.domain.user;

import lombok.RequiredArgsConstructor;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class UserCashManager {

    private final UserRepository userRepository;

    @Transactional
    @Retryable(
            retryFor = {ObjectOptimisticLockingFailureException.class},
            maxAttempts = 5,
            backoff = @Backoff(delay = 200)
    )
    public void chargeAmount(Long userId, Integer amount) {
        User user = userRepository.findById(userId);
        user.addAmount(amount);
    }

    @Transactional(readOnly = true)
    public Integer selectAmount(Long userId) {
        User user = userRepository.findById(userId);
        return user.getAmount();
    }

}
