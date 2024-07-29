package hhplus.concert.domain.userqueue;

import hhplus.concert.interfaces.api.support.ApiException;
import hhplus.concert.interfaces.api.support.error.ErrorCode;
import hhplus.concert.support.holder.TimeHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.logging.LogLevel;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserQueueService {

    private final UserQueueRepository userQueueRepository;
    private final UserQueueTokenProvider userQueueTokenProvider;
    private final TimeHolder timeHolder;
    private final UserQueueConstant userQueueConstant;

    @Transactional
    public String enterUserQueue(Long concertScheduleId, Long userId) {
        Optional<UserQueue> optUserQueue = userQueueRepository.findBy(concertScheduleId, userId);
        if (optUserQueue.isPresent()) {
            return optUserQueue.get().getToken();
        }
        String queueToken = userQueueTokenProvider.createQueueToken();
        userQueueRepository.save(concertScheduleId, userId, queueToken);
        return queueToken;
    }

    /**
     * 토큰 발급 후 대기열 번호를 반환
     * - 대기열이 꽉 차지 않았을 경우 0을 반환
     * - 대기열이 꽉 찼을 경우 대기 순번을 계산하여 반환
     */
    @Transactional
    public Integer selectWaitingNumber(String token, Long concertScheduleId) {
        List<UserQueue> progressingUserQueues = userQueueRepository.findStatusIsProgressBy(concertScheduleId);

        /**
         * 대기열을 통과할 수 있을 경우 대기열 토큰 진행 상태 = PROGRESS, 만료 시간 =  현재시간 + 30분 업데이트 후 0을 반환
         */
        if (progressingUserQueues.size() < userQueueConstant.getMaxWaitingNumber()) {
            LocalDateTime expiredAt = timeHolder.currentDateTime().plusMinutes(userQueueConstant.getQueueTokenExpireTime());
            userQueueRepository.updateStatusAndExpiredAt(UserQueueStatus.PROGRESS, expiredAt, token);
            return 0;
        }

        /**
         * 대기열이 꽉 찼을 경우 대기 순번을 계산하여 반환
         * - 대기 상태가 WAITING이며 요청한 유저보다 진입 시점이 적은 토큰 조회
         * - 대기 순번 = 조회된 토큰 수 + 1
         */
        UserQueue userQueue = userQueueRepository.findByOrElseThrow(token);
        List<UserQueue> waitingUserQueues = userQueueRepository.findStatusIsWaitingAndAlreadyEnteredBy(concertScheduleId, userQueue.getEnteredAt());
        return waitingUserQueues.size() + 1;
    }

    @Transactional(readOnly = true)
    public void validateTopExpiredBy(String token) {
        UserQueue userQueue = userQueueRepository.findByOrElseThrow(token);
        if (userQueue.isExpired()) {
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
