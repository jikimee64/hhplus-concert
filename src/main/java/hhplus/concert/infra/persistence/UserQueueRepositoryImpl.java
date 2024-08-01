package hhplus.concert.infra.persistence;

import hhplus.concert.domain.userqueue.UserQueue;
import hhplus.concert.domain.userqueue.UserQueueRepository;
import hhplus.concert.domain.userqueue.UserQueueStatus;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserQueueRepositoryImpl implements UserQueueRepository {

    private final UserQueueJpaRepository userQueueJpaRepository;
    private final WaitingQueueRedisRepository waitingQueueRedisRepository;
    private final ActiveQueueRedisRepository activeQueueRedisRepository;

    @Override
    public void save(Long userId, Long concertScheduleId, String token) {
        waitingQueueRedisRepository.add(concertScheduleId, token);
    }

    @Override
    public Boolean isActiveToken(String token) {
        return activeQueueRedisRepository.exists(token);
    }

    @Override
    public void deleteActiveToken(String token) {
        activeQueueRedisRepository.delete(token);
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
