package hhplus.concert.domain;

import hhplus.concert.IntegrationTest;
import hhplus.concert.infra.jwt.JwtQueueTokenProviderTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class UserQueueManagerTest extends IntegrationTest {

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

    @Test
    void 제한인원이_50명인_큐에서_PROGRESS_상태인_마지막_대기열_번호가_50번이고_유저의_대기열_번호가_51일_경우_대기순번_1을_반환한다() {
        // given
        Long userId = 1L;
        Long userWaitingNumber = 51L;
        QueueTokenProvider queueTokenProvider = new JwtQueueTokenProviderTest(userId, userWaitingNumber);
        UserQueueManager userQueueManager = new UserQueueManager(userQueueRepository, queueTokenProvider);
        Long concertScheduleId = 1L;

        // when
        Long waitingNumber = userQueueManager.selectWaitingNumber(concertScheduleId, userId, userWaitingNumber);

        // then
        assertThat(waitingNumber).isEqualTo(1L);
    }

}
