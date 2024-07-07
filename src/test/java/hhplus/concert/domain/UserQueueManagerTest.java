package hhplus.concert.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@SpringBootTest
class UserQueueManagerTest {

    @Autowired
    private UserQueueManager userQueueManager;

    @Test
    void 대기열에_유저를_등록하고_대기열_토큰을_반환한다(){
        // given
        Long userId = 1L;
        Long concertScheduleId = 1L;

        // when
        String queueToken = userQueueManager.enterUserQueue(concertScheduleId, userId);

        // then
        assertThat(queueToken).isEqualTo("");
    }

}
