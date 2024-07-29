package hhplus.concert.domain.user;

import hhplus.concert.infra.persistence.UserJpaRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@SpringBootTest
@ActiveProfiles("test")
public class UserCashManagerTest {

    @Autowired
    private UserJpaRepository userJpaRepository;

    @Autowired
    private UserCashManager userCashManager;

    @Autowired
    private EntityManager entityManager;

    @AfterEach
    void tearDown() {
        userJpaRepository.deleteAll();
    }

    @Test
    void 유저가_잔액을_충전한다() {
        // given
        User savedUser = userJpaRepository.save(new User("loginId"));

        // when
        userCashManager.chargeAmount(savedUser.getId(), 1000);
        userCashManager.chargeAmount(savedUser.getId(), 2000);

        entityManager.clear();
        User selectedUser = userJpaRepository.findById(savedUser.getId()).get();

        // then
        assertThat(selectedUser.getAmount()).isEqualTo(3000);
    }

    @Test
    void 유저가_잔액을_조회한다() {
        // given
        User savedUser = userJpaRepository.save(new User("loginId"));

        // when
        userCashManager.chargeAmount(savedUser.getId(), 1000);
        userCashManager.chargeAmount(savedUser.getId(), 2000);

        // then
        assertThat(userCashManager.selectAmount(savedUser.getId())).isEqualTo(3000);
    }

}
