package hhplus.concert.domain;

import hhplus.concert.IntegrationTest;
import hhplus.concert.infra.persistence.UserJpaRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@Transactional
class UserCashManagerTest extends IntegrationTest {

    @Autowired
    private UserJpaRepository userJpaRepository;

    @Autowired
    private UserCashManager userCashManager;

    @Test
    void 유저가_잔액을_충전한다() {
        // given
        User savedUser = userJpaRepository.save(new User("userId"));

        // when
        userCashManager.chargeAmount(savedUser.getId(), 1000);
        userCashManager.chargeAmount(savedUser.getId(), 2000);

        // then
        assertThat(savedUser.getAmount()).isEqualTo(3000);
    }

    @Test
    void 유저가_잔액을_조회한다() {
        // given
        User savedUser = userJpaRepository.save(new User("userId"));

        // when
        userCashManager.chargeAmount(savedUser.getId(), 1000);
        userCashManager.chargeAmount(savedUser.getId(), 2000);

        // then
        assertThat(userCashManager.selectAmount(savedUser.getId())).isEqualTo(3000);
    }

}
