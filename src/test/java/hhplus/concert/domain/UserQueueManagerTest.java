package hhplus.concert.domain;

import hhplus.concert.infra.jwt.JwtQueueTokenProviderTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest
class UserQueueManagerTest {

    @Autowired
    private UserQueueRepository userQueueRepository;

    @Test
    void 대기열에_유저를_등록하고_대기열_토큰을_반환한다() {
        // given
        String testQueueToken = "testQueueToken";
        QueueTokenProvider queueTokenProvider = new JwtQueueTokenProviderTest(testQueueToken);
        UserQueueManager userQueueManager = new UserQueueManager(userQueueRepository, queueTokenProvider);
        Long userId = 1L;
        Long concertScheduleId = 1L;

        // when
        String queueToken = userQueueManager.enterUserQueue(concertScheduleId, userId);

        // then
        List<UserQueue> userQueues = userQueueRepository.findAll();
        assertAll(
                () -> assertThat(testQueueToken).isEqualTo(queueToken),
                () -> assertThat(userQueues).hasSize(1)
        );
    }

}
