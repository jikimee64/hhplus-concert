package hhplus.concert.infra.persistence;

import hhplus.concert.IntegrationTest;
import hhplus.concert.domain.UserQueue;
import hhplus.concert.domain.UserQueueStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.tuple;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class UserQueueJpaRepositoryTest extends IntegrationTest {

    @Autowired
    private UserQueueJpaRepository userQueueJpaRepository;

    @Test
    @Transactional
    void 특정_콘서트의_대기열_토큰_에서_상태가_PROGRESS인_대기열을_모두_조회한다() {
        // given
        saveUserQueue();

        Long concertScheduleId = 1L;
        UserQueueStatus status = UserQueueStatus.PROGRESS;

        // when
        List<UserQueue> userQueues = userQueueJpaRepository.findOrderByIdDescBy(
                concertScheduleId,
                status
        );

        // then
        assertThat(userQueues).hasSize(2)
                .extracting("id", "concertScheduleId", "status")
                .containsExactly(
                        tuple(3L, concertScheduleId, UserQueueStatus.PROGRESS),
                        tuple(1L, concertScheduleId, UserQueueStatus.PROGRESS)
                );
    }

    private void saveUserQueue() {
        userQueueJpaRepository.saveAll(
                List.of(
                    new UserQueue(1L, 1L, UserQueueStatus.PROGRESS),
                    new UserQueue(2L, 2L, UserQueueStatus.WAITING),
                    new UserQueue(3L, 1L, UserQueueStatus.PROGRESS),
                    new UserQueue(4L, 1L, UserQueueStatus.DONE),
                    new UserQueue(5L, 1L, UserQueueStatus.EXPIRED)
                )
        );
    }

}
