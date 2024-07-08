package hhplus.concert.domain;

import hhplus.concert.IntegrationTest;
import hhplus.concert.infra.jwt.JwtQueueTokenProviderTest;
import hhplus.concert.infra.persistence.UserQueueJpaRepository;
import hhplus.concert.support.holder.TestTimeHolder;
import hhplus.concert.support.holder.TimeHolder;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@Transactional
class UserQueueManagerTest extends IntegrationTest {

    @Autowired
    private UserQueueRepository userQueueRepository;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private UserQueueJpaRepository userQueueJpaRepository;

    private TimeHolder timeHolder = new TestTimeHolder();

    @Test
    void 대기열에_유저를_등록하고_대기열_토큰을_반환한다() {
        // given
        String testQueueToken = "testQueueToken";
        QueueTokenProvider queueTokenProvider = new JwtQueueTokenProviderTest(testQueueToken);
        UserQueueManager userQueueManager = new UserQueueManager(userQueueRepository, queueTokenProvider, timeHolder);
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
    void 제한인원이_50명인_큐에서_PROGRESS_상태인_대기열이_50명_미만일_경우_상태값과_만료시간을_업데이트_후_대기순번_0을_반환한다() {
        // given
        Long userId = 1L;
        Long userWaitingNumber = 51L;
        Long concertScheduleId = 1L;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.parse("2024-01-01 00:00:00", formatter);
        UserQueue savedUserQueue = userQueueJpaRepository.save(new UserQueue(userId, concertScheduleId, UserQueueStatus.PROGRESS, dateTime, null));

        TimeHolder testTimeHolder = new TestTimeHolder(dateTime);
        QueueTokenProvider queueTokenProvider = new JwtQueueTokenProviderTest(userId, userWaitingNumber);
        UserQueueManager userQueueManager = new UserQueueManager(userQueueRepository, queueTokenProvider, testTimeHolder);

        // when
        Long waitingNumber = userQueueManager.selectWaitingNumber(concertScheduleId, userId);

        entityManager.clear(); // 디비에 업데이트 된 값을 가져오기 위해 영속성 컨텍스트 초기화

        // then
        UserQueue userQueue = userQueueJpaRepository.findById(savedUserQueue.getId()).get();

        assertAll(
                () -> assertThat(waitingNumber).isEqualTo(0L),
                () -> assertThat(userQueue.getExpiredAt()).isEqualTo(dateTime.plusMinutes(30)),
                () -> assertThat(userQueue.getStatus()).isEqualTo(UserQueueStatus.PROGRESS)
        );
    }

    @Disabled
    @Test
    void 제한인원이_50명인_큐에서_PROGRESS_상태인_마지막_대기열_번호가_50번이고_유저의_대기열_번호가_51일_경우_대기순번_1을_반환한다() {
        // given
        Long userId = 1L;
        Long userWaitingNumber = 51L;
        QueueTokenProvider queueTokenProvider = new JwtQueueTokenProviderTest(userId, userWaitingNumber);
        UserQueueManager userQueueManager = new UserQueueManager(userQueueRepository, queueTokenProvider, timeHolder);
        Long concertScheduleId = 1L;

        // when
        Long waitingNumber = userQueueManager.selectWaitingNumber(concertScheduleId, userId);

        // then
        assertThat(waitingNumber).isEqualTo(1L);
    }

}
