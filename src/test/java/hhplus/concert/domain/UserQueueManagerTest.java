package hhplus.concert.domain;

import hhplus.concert.IntegrationTest;
import hhplus.concert.api.support.ApiException;
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

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@Transactional
class UserQueueManagerTest extends IntegrationTest {

    @Autowired
    private UserQueueRepository userQueueRepository;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private UserQueueConstant userQueueConstant;

    @Autowired
    private UserQueueJpaRepository userQueueJpaRepository;

    private TimeHolder timeHolder = new TestTimeHolder();

    @Test
    void 대기열에_유저를_등록하고_대기열_토큰을_반환한다() {
        // given
        String testQueueToken = "testQueueToken";
        QueueTokenProvider queueTokenProvider = new JwtQueueTokenProviderTest(testQueueToken);
        UserQueueManager userQueueManager = new UserQueueManager(userQueueRepository, queueTokenProvider, timeHolder, userQueueConstant);
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
    void 제한인원이_5명인_큐에서_PROGRESS_상태인_대기열이_5명_미만일_경우_상태값과_만료시간을_업데이트_후_대기순번_0을_반환한다() {
        // given
        Long userId = 1L;
        Long userWaitingNumber = 51L;
        Long concertScheduleId = 1L;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.parse("2024-01-01 00:00:00", formatter);
        UserQueue savedUserQueue = userQueueJpaRepository.save(new UserQueue(userId, concertScheduleId, UserQueueStatus.PROGRESS, dateTime, null));

        TimeHolder testTimeHolder = new TestTimeHolder(dateTime);
        QueueTokenProvider queueTokenProvider = new JwtQueueTokenProviderTest(userId, userWaitingNumber);
        UserQueueManager userQueueManager = new UserQueueManager(userQueueRepository, queueTokenProvider, testTimeHolder, userQueueConstant);

        // when
        Integer waitingNumber = userQueueManager.selectWaitingNumber(concertScheduleId, userId);

        entityManager.clear(); // 디비에 업데이트 된 값을 가져오기 위해 영속성 컨텍스트 초기화

        // then
        UserQueue userQueue = userQueueJpaRepository.findById(savedUserQueue.getId()).get();

        assertAll(
                () -> assertThat(waitingNumber).isEqualTo(0L),
                () -> assertThat(userQueue.getExpiredAt()).isEqualTo(dateTime.plusMinutes(30)),
                () -> assertThat(userQueue.getStatus()).isEqualTo(UserQueueStatus.PROGRESS)
        );
    }

    @Test
    void 제한인원이_5명인_큐에서_5명이_PROGRESS이고_2명이_WAITING인_경우_대기순번_3을_반환한다() {
        // given
        saveUserQueue();
        Long userId = 1L;
        Long userQueuePk = 10L;
        QueueTokenProvider queueTokenProvider = new JwtQueueTokenProviderTest(userId, userQueuePk);
        UserQueueManager userQueueManager = new UserQueueManager(userQueueRepository, queueTokenProvider, timeHolder, userQueueConstant);
        Long concertScheduleId = 1L;

        // when
        Integer waitingNumber = userQueueManager.selectWaitingNumber(concertScheduleId, userId);

        // then
        assertThat(waitingNumber).isEqualTo(3L);
    }

    @Test
    void 대기열_상태가_EXPIRED일_경우_예외가_발생한다() {
        // given
        Long userId = 1L;
        Long concertScheduleId = 1L;
        userQueueJpaRepository.save(new UserQueue(userId, concertScheduleId, UserQueueStatus.EXPIRED));
        QueueTokenProvider queueTokenProvider = new JwtQueueTokenProviderTest(userId, 1L);
        UserQueueManager userQueueManager = new UserQueueManager(userQueueRepository, queueTokenProvider, timeHolder, userQueueConstant);

        // when & then
        assertThatThrownBy(() -> userQueueManager.validateTopExpiredBy(concertScheduleId, userId))
                .isInstanceOf(ApiException.class)
                .hasMessage("대기열 상태가 활성상태가 아닙니다.");
    }

    private void saveUserQueue() {
        userQueueJpaRepository.saveAll(
                List.of(
                        new UserQueue(1L, 1L, UserQueueStatus.PROGRESS),
                        new UserQueue(2L, 1L, UserQueueStatus.PROGRESS),
                        new UserQueue(3L, 1L, UserQueueStatus.PROGRESS),
                        new UserQueue(4L, 1L, UserQueueStatus.PROGRESS),
                        new UserQueue(5L, 1L, UserQueueStatus.PROGRESS),
                        new UserQueue(6L, 1L, UserQueueStatus.WAITING),
                        new UserQueue(7L, 1L, UserQueueStatus.WAITING)
                )
        );
    }

}
