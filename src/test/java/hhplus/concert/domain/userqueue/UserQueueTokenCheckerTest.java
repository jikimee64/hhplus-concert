package hhplus.concert.domain.userqueue;

import hhplus.concert.IntegrationTest;
import hhplus.concert.infra.persistence.UserQueueJpaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class UserQueueTokenCheckerTest extends IntegrationTest {

    @Autowired
    private UserQueueTokenChecker userQueueTokenChecker;

    @Autowired
    private UserQueueJpaRepository userQueueJpaRepository;

    @Test
    void 대기열_토큰_상태가_PROGRESS인_토큰중_만료_시간값이_30분이_지났을_경우_EXPIRE로_업데이트한다() {
        // given
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        LocalDateTime enteredAt = LocalDateTime.parse("2024-01-01 00:00:00", formatter);
        LocalDateTime expiredAt = LocalDateTime.parse("2024-01-01 00:30:01", formatter);

        userQueueJpaRepository.save(
                new UserQueue(1L, 1L, "", UserQueueStatus.PROGRESS, enteredAt, expiredAt)
        );

        // when
        Integer updatedCount = userQueueTokenChecker.updateExpireConditionToken();

        // then
        assertThat(updatedCount).isEqualTo(1);
    }

}
