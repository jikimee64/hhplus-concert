package hhplus.concert.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class UserCashManager {

    private final UserRepository userRepository;

    @Transactional
    public void chargeAmount(Long userId, int amount) {
        User user = userRepository.findById(userId);
        user.addAmount(amount);
    }

    @Transactional(readOnly = true)
    public int selectAmount(Long userId) {
        User user = userRepository.findById(userId);
        return user.getAmount();
    }

}
