package hhplus.concert.domain.userqueue;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class UserQueueTest {

    @Test
    void 대기열_토큰이_만료된_경우_true를_반환한다() {
        // given
        LocalDateTime now = LocalDateTime.now();
        UserQueue userQueue = new UserQueue(1L, 1L, "", now, UserQueueStatus.EXPIRED);

        // when
        boolean expired = userQueue.isExpired();

        // then
        assertThat(expired).isEqualTo(true);
    }

    @Test
    void 대기열_토큰의_상태값을_변경한다() {
        // given
        LocalDateTime now = LocalDateTime.now();
        UserQueue userQueue = new UserQueue(1L, 1L, "", now, UserQueueStatus.WAITING);

        // when
        userQueue.updateStatusDone(UserQueueStatus.DONE);

        // then
        assertThat(userQueue.getStatus()).isEqualTo(UserQueueStatus.DONE);
    }

}
