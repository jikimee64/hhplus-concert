package hhplus.concert.domain;

import hhplus.concert.support.holder.TimeHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class UserQueueManager {

    private final Long MAX_WAITING_NUMBER = 100L;
    private final Long QUEUE_TOKEN_EXPIRE_TIME_MINUTE = 30L;

    private final UserQueueRepository userQueueRepository;
    private final QueueTokenProvider queueTokenProvider;
    private final TimeHolder timeHolder;

    @Transactional
    public String enterUserQueue(Long concertScheduleId, Long userId) {
        UserQueue savedUserQueue = userQueueRepository.save(concertScheduleId, userId);
        Long waitingNumber = savedUserQueue.getId();
        return queueTokenProvider.createQueueToken(userId, waitingNumber);
    }

    /**
     * 토큰 발급 후 대기열 번호를 반환
     * - 대기열이 꽉 차지 않았을 경우 0을 반환
     * - 대기열이 꽉 찼을 경우 대기 순번을 계산하여 반환
     */
    @Transactional
    public Long selectWaitingNumber(Long concertScheduleId, Long userId) {
        List<UserQueue> userQueues = userQueueRepository.findStatusIsProgressBy(concertScheduleId);

        /**
         * 대기열을 통과할 수 있을 경우 대기열 토큰 진행 상태 = PROGRESS, 만료 시간 =  현재시간 + 30분 업데이트 후 0을 반환
         */
        if(userQueues.size() < MAX_WAITING_NUMBER){
            LocalDateTime expiredAt = timeHolder.currentDateTime().plusMinutes(QUEUE_TOKEN_EXPIRE_TIME_MINUTE);
            userQueueRepository.updateStatusAndExpiredAt(UserQueueStatus.PROGRESS, expiredAt, userId, concertScheduleId);
            return 0L;
        }

        return 1L;
    }
}
