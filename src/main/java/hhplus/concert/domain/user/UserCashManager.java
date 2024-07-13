package hhplus.concert.domain.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class UserCashManager {

    private final UserRepository userRepository;

    @Transactional
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
