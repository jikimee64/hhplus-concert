package hhplus.concert.application.user;

import hhplus.concert.domain.user.UserCashService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserFacade {

    private final UserCashService userCashService;

    public void chargeAmount(Long userId, Integer amount) {
        userCashService.chargeAmount(userId, amount);
    }

    public Integer selectAmount(Long userId) {
        return userCashService.selectAmount(userId);
    }
}
