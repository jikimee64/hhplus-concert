package hhplus.concert.domain.user;

import hhplus.concert.infra.persistence.UserJpaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@ActiveProfiles("test")
@SpringBootTest
public class UserCashManagerConcurrencyTest {

    @Autowired
    private UserCashManager userCashManager;

    @Autowired
    private UserJpaRepository userJpaRepository;

    @Test
    void 잔액_충전_동시성_테스트() throws InterruptedException {
        // given
        User savedUser = userJpaRepository.save(new User("dncjf64", 10000));

        // when
        CompletableFuture.allOf(
                CompletableFuture.runAsync(() -> {
                    userCashManager.chargeAmount(savedUser.getId(), 1000);
                }),
                CompletableFuture.runAsync(() -> {
                    userCashManager.chargeAmount(savedUser.getId(), 2000);
                }),
                CompletableFuture.runAsync(() -> {
                    userCashManager.chargeAmount(savedUser.getId(), 3000);
                })
        ).join();

        Thread.sleep(100L);

        // then
        Integer amount = userCashManager.selectAmount(savedUser.getId());
        assertThat(amount).isEqualTo(10000 + 1000 + 2000 + 3000);
    }

}
