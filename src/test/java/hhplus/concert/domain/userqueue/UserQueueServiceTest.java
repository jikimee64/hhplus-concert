package hhplus.concert.domain.userqueue;

import hhplus.concert.IntegrationTest;
import hhplus.concert.domain.concert.Concert;
import hhplus.concert.domain.concert.ConcertRepository;
import hhplus.concert.domain.concert.ConcertSchedule;
import hhplus.concert.domain.concert.TotalSeatStatus;
import hhplus.concert.infra.persistence.ActiveQueueRedisRepository;
import hhplus.concert.infra.persistence.ConcertJpaRepository;
import hhplus.concert.infra.persistence.ConcertScheduleJpaRepository;
import hhplus.concert.infra.persistence.WaitingQueueRedisRepository;
import hhplus.concert.interfaces.api.support.ApiException;
import hhplus.concert.infra.tokenprovider.UuidUserQueueTokenProviderTest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class UserQueueServiceTest extends IntegrationTest {

    @Autowired
    private UserQueueRepository userQueueRepository;

    @Autowired
    private ConcertRepository concertRepository;

    @Autowired
    private ConcertJpaRepository concertJpaRepository;

    @Autowired
    private ConcertScheduleJpaRepository concertScheduleJpaRepository;

    @Autowired
    private UserQueueConstant userQueueConstant;

    @Autowired
    private ActiveQueueRedisRepository activeQueueRedisRepository;

    @Autowired
    private WaitingQueueRedisRepository waitingQueueRedisRepository;

    @BeforeEach
    void setUp() {
        waitingQueueRedisRepository.deleteAll();
    }

    @Test
    void 대기열에_존재하지_않을경우_유저를_등록하고_대기열_토큰을_반환한다() {
        // given
        String testQueueToken = "testQueueToken";
        UserQueueTokenProvider userQueueTokenProvider = new UuidUserQueueTokenProviderTest(testQueueToken);
        UserQueueService userQueueService = new UserQueueService(concertRepository, userQueueRepository, userQueueTokenProvider, userQueueConstant);
        Long userId = 1L;
        Long concertScheduleId = 1L;

        // when
        String queueToken = userQueueService.enterUserQueue(concertScheduleId, userId);

        // then
        boolean exists = waitingQueueRedisRepository.existsWithMember(concertScheduleId, queueToken);
        assertThat(exists).isTrue();
    }

    @Test
    void 대기열_토큰에_없을_경우_대기순번_0을_반환한다() {
        // given
        Long userId = 1L;
        Long userWaitingNumber = 51L;
        Long concertScheduleId = 1L;
        UserQueueTokenProvider userQueueTokenProvider = new UuidUserQueueTokenProviderTest(userId, userWaitingNumber);
        UserQueueService userQueueService = new UserQueueService(concertRepository, userQueueRepository, userQueueTokenProvider, userQueueConstant);

        // when
        Long waitingNumber = userQueueService.selectWaitingNumber("", concertScheduleId);

        // then
        assertThat(waitingNumber).isEqualTo(0L);
    }

    @Test
    void 대기열_토큰에_있을_경우_대기순번을_반환한다() {
        // given
        Long userId = 1L;
        Long userWaitingNumber = 51L;
        Long concertScheduleId = 1L;
        UserQueueTokenProvider userQueueTokenProvider = new UuidUserQueueTokenProviderTest(userId, userWaitingNumber);
        UserQueueService userQueueService = new UserQueueService(concertRepository, userQueueRepository, userQueueTokenProvider, userQueueConstant);

        // when
        waitingQueueRedisRepository.add(concertScheduleId, "1");

        Long waitingNumber = userQueueService.selectWaitingNumber("1", concertScheduleId);

        // then
        assertThat(waitingNumber).isEqualTo(1L);
    }

    @Test
    void 활성화_토큰에_없을경우_예외가_발생한다() {
        // given
        Long userId = 1L;
        UserQueueTokenProvider userQueueTokenProvider = new UuidUserQueueTokenProviderTest(userId, 1L);
        UserQueueService userQueueService = new UserQueueService(concertRepository, userQueueRepository, userQueueTokenProvider, userQueueConstant);

        // when & then
        assertThatThrownBy(() -> userQueueService.validateTopExpiredBy(""))
                .isInstanceOf(ApiException.class)
                .hasMessage("대기열 상태가 활성상태가 아닙니다.");
    }

    @Test
    void 대기열_토큰을_꺼내서_활성화_토큰으로_적재_시킨다() {
        // given
        Long userId = 1L;
        UserQueueTokenProvider userQueueTokenProvider = new UuidUserQueueTokenProviderTest(userId, 1L);
        UserQueueService userQueueService = new UserQueueService(concertRepository, userQueueRepository, userQueueTokenProvider, userQueueConstant);

        Concert savedConcert1 = concertJpaRepository.save(new Concert("콘서트1"));
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDate openDate = LocalDate.parse("2024-01-01", dateFormatter);
        LocalDateTime localDateTime = LocalDateTime.parse("2024-01-01 10:00:00", dateTimeFormatter);
        int totalSeat = 50;

        ConcertSchedule savedConcertSchedule = concertScheduleJpaRepository.save(
            new ConcertSchedule(savedConcert1, openDate, localDateTime, localDateTime.plusHours(1L), totalSeat, TotalSeatStatus.AVAILABLE)
        );
        Long concertScheduleId = savedConcertSchedule.getId();

        waitingQueueRedisRepository.add(concertScheduleId, "token1");
        waitingQueueRedisRepository.add(concertScheduleId, "token2");

        // when
        userQueueService.periodicallyEnterUserQueue();

        boolean isWaitingToken = waitingQueueRedisRepository.existsWithMember(concertScheduleId, "token1");
        boolean isWaitingToken2 = waitingQueueRedisRepository.existsWithMember(concertScheduleId, "token2");

        boolean isActiveToken = activeQueueRedisRepository.exists("token1");
        boolean isActiveToken2 = activeQueueRedisRepository.exists("token2");

        // then
        assertAll(
            () -> assertThat(isWaitingToken).isFalse(),
            () -> assertThat(isWaitingToken2).isFalse(),
            () -> assertThat(isActiveToken).isTrue(),
            () -> assertThat(isActiveToken2).isTrue()
        );
    }

}
