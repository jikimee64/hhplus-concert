package hhplus.concert.application;

import hhplus.concert.domain.UserCashManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserService {

    private final UserCashManager userCashManager;

    public void chargeAmount(Long userId, Integer amount) {
        userCashManager.chargeAmount(userId, amount);
    }

    public Integer selectAmount(Long userId) {
        return userCashManager.selectAmount(userId);
    }
}
