package hhplus.concert.infra.persistence;

import hhplus.concert.interfaces.api.support.ApiException;
import hhplus.concert.interfaces.api.support.error.ErrorCode;
import hhplus.concert.domain.userqueue.UserQueue;
import hhplus.concert.domain.userqueue.UserQueueRepository;
import hhplus.concert.domain.userqueue.UserQueueStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.logging.LogLevel;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserQueueRepositoryImpl implements UserQueueRepository {

    private final UserQueueJpaRepository userQueueJpaRepository;
    private final WaitingQueueRedisRepository waitingQueueRedisRepository;

    @Override
    public void save(Long userId, Long concertScheduleId, String token) {
        waitingQueueRedisRepository.add(concertScheduleId, token);
    }

    @Override
    public UserQueue findByOrElseThrow(String token) {
        return userQueueJpaRepository.findByToken(token)
                .orElseThrow(() -> new ApiException(ErrorCode.E404, LogLevel.INFO, "UserQueue not found. token = " + token));
    }

    @Override
    public Long waitingNumber(Long concertScheduleId, String token) {
        return waitingQueueRedisRepository.rank(concertScheduleId, token);
    }

    @Override
    public List<UserQueue> findAllBy(UserQueueStatus status) {
        return userQueueJpaRepository.findAllByStatus(status);
    }

    @Override
    public List<UserQueue> findAllWaitingBy(Long concertScheduleId, Integer limitSize) {
        return userQueueJpaRepository.findAllLimitSize(concertScheduleId, UserQueueStatus.WAITING, limitSize);
    }

    @Override
    public Integer updateStatusByIds(List<Long> userQueueIds, UserQueueStatus status) {
        return userQueueJpaRepository.updateStatusByIds(status, userQueueIds);
    }

    @Override
    public Integer updateExpireConditionToken() {
        return userQueueJpaRepository.updateStatusExpire(
                UserQueueStatus.EXPIRED,
                UserQueueStatus.PROGRESS,
                LocalDateTime.now()
        );
    }

    @Override
    public List<UserQueue> findAll() {
        return userQueueJpaRepository.findAll();
    }

}
