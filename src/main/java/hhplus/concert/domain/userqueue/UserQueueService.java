package hhplus.concert.domain.userqueue;

import hhplus.concert.domain.concert.ConcertRepository;
import hhplus.concert.domain.concert.ConcertSchedule;
import hhplus.concert.interfaces.api.support.ApiException;
import hhplus.concert.interfaces.api.support.error.ErrorCode;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.logging.LogLevel;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserQueueService {

    private final ConcertRepository concertRepository;
    private final UserQueueRepository userQueueRepository;
    private final UserQueueTokenProvider userQueueTokenProvider;
    private final UserQueueConstant userQueueConstant;

    @Transactional
    public String enterUserQueue(Long concertScheduleId, Long userId) {
        String queueToken = userQueueTokenProvider.createQueueToken();
        userQueueRepository.save(concertScheduleId, userId, queueToken);
        return queueToken;
    }

    /**
     * 대기열 큐에 존재하지 않을 경우
     * - 활성화된 큐에 없을 경우 예외처리
     * - 활성화된 큐에 있을 경우 0을 반환
     * 대기열 큐에 존재할 경우 해당해는 순서 반환
     */
    @Transactional
    public Long selectWaitingNumber(String token, Long concertScheduleId) {
        Long waitingNumber = userQueueRepository.waitingNumber(concertScheduleId, token);
        if (waitingNumber == 0) {
            Boolean existActiveToken = userQueueRepository.existActiveToken(token);
            if(!existActiveToken){
                throw new ApiException(ErrorCode.E003, LogLevel.INFO, "token = " + token);
            }
            return 0L;
        }
        return waitingNumber;
    }

    @Transactional(readOnly = true)
    public void validateTopExpiredBy(String token) {
        Boolean isExist = userQueueRepository.isActiveToken(token);
        if(!isExist){
            throw new ApiException(ErrorCode.E001, LogLevel.INFO, "token = " + token);
        }
    }

    /**
     * 콘서트 스케줄 마다 대기열 제한인원에 허용하는 만큼 대기중인 유저를 진입시킨다
     */
    @Transactional
    public void periodicallyEnterUserQueue() {
        List<ConcertSchedule> concertSchedules = concertRepository.findConcertSchedules();

        for (ConcertSchedule concertSchedule : concertSchedules) {
            Long maxWaitingSize = userQueueConstant.getMaxWaitingNumber() - 1;
            Set<String> waitingTokenRange = userQueueRepository.getWaitingTokenRange(concertSchedule.getId(), 0L, maxWaitingSize);
            userQueueRepository.deleteWaitingToken(concertSchedule.getId(), waitingTokenRange);
            waitingTokenRange.forEach(token -> userQueueRepository.addActiveToken(token, concertSchedule.getId()));
        }
    }

    @Transactional
    public void deleteActiveToken(String token) {
        userQueueRepository.deleteActiveToken(token);
    }

}
