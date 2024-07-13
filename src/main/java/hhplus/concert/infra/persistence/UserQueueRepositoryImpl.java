package hhplus.concert.infra.persistence;

import hhplus.concert.interfaces.api.support.ApiException;
import hhplus.concert.interfaces.api.support.error.ErrorCode;
import hhplus.concert.domain.userqueue.UserQueue;
import hhplus.concert.domain.userqueue.UserQueueRepository;
import hhplus.concert.domain.userqueue.UserQueueStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserQueueRepositoryImpl implements UserQueueRepository {

    private final UserQueueJpaRepository userQueueJpaRepository;

    @Override
    public UserQueue save(Long userId, Long concertScheduleId, String token) {
        return userQueueJpaRepository.save(
                new UserQueue(userId, concertScheduleId, token)
        );
    }

    @Override
    public UserQueue findByOrElseThrow(String token) {
        return userQueueJpaRepository.findByToken(token)
                .orElseThrow(() -> new ApiException(ErrorCode.E404, "UserQueue not found. token = " + token));
    }

    @Override
    public Optional<UserQueue> findBy(Long userId, Long concertScheduleId) {
        return userQueueJpaRepository.findByUserIdAndConcertScheduleId(userId, concertScheduleId);
    }

    @Override
    public List<UserQueue> findStatusIsProgressBy(Long concertScheduleId) {
        return userQueueJpaRepository.findOrderByIdDescBy(concertScheduleId, UserQueueStatus.PROGRESS);
    }

    @Override
    public List<UserQueue> findStatusIsWaitingAndAlreadyEnteredBy(Long concertScheduleId, LocalDateTime enteredAt) {
        return userQueueJpaRepository.findOrderByIdDescBy(concertScheduleId, UserQueueStatus.WAITING, enteredAt);
    }

    @Override
    public Integer updateStatusAndExpiredAt(UserQueueStatus status, LocalDateTime expiredAt, String token) {
        return userQueueJpaRepository.updateStatusAndExpiredAt(status, expiredAt, token);
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
