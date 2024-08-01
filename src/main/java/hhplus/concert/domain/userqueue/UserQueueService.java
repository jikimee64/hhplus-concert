package hhplus.concert.domain.userqueue;

import hhplus.concert.interfaces.api.support.ApiException;
import hhplus.concert.interfaces.api.support.error.ErrorCode;
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
     * 대기열 큐에 존재하지 않을 경우 0을 반환
     * - 활성화된 큐에 추가 되었다는 의미
     * 대기열 큐에 존재할 경우 해당해는 순서 ㅂ나환
     */
    @Transactional
    public Long selectWaitingNumber(String token, Long concertScheduleId) {
        return userQueueRepository.waitingNumber(concertScheduleId, token) ;
    }

    @Transactional(readOnly = true)
    public void validateTopExpiredBy(String token) {
        Boolean isExist = userQueueRepository.isActiveToken(token);
        if(!isExist){
            throw new ApiException(ErrorCode.E001, LogLevel.INFO, "token = " + token);
        }
    }

    /**
     * 토큰 만료 여부 조건일 경우 토큰 상태값을 업데이트 한다.
     */
    @Transactional
    public Integer updateExpireConditionToken() {
        return userQueueRepository.updateExpireConditionToken();
    }

    /**
     * 콘서트 스케줄 마다 대기열 제한인원에 허용하는 만큼 대기중인 유저를 진입시킨다
     */
    @Transactional
    public void periodicallyEnterUserQueue() {
        Map<Long, List<UserQueue>> groupingUserQueue = userQueueRepository.findAllBy(UserQueueStatus.PROGRESS).stream()
                .collect(Collectors.groupingBy(UserQueue::getConcertScheduleId, Collectors.toList()));

        groupingUserQueue.forEach(
                (concertScheduleId, userQueues) -> {
                    Integer remainEnteringSize = userQueueConstant.getMaxWaitingNumber() - userQueues.size();
                    if (remainEnteringSize > 0) {
                        List<UserQueue> waitingUserQueues = userQueueRepository.findAllWaitingBy(concertScheduleId, remainEnteringSize);
                        userQueueRepository.updateStatusByIds(waitingUserQueues.stream().map(UserQueue::getId).toList(), UserQueueStatus.PROGRESS);
                    }
                }
        );
    }

}
