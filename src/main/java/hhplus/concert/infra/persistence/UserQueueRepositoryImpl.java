package hhplus.concert.infra.persistence;

import hhplus.concert.domain.UserQueue;
import hhplus.concert.domain.UserQueueRepository;
import hhplus.concert.domain.UserQueueStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserQueueRepositoryImpl implements UserQueueRepository {

    private final UserQueueJpaRepository userQueueJpaRepository;

    @Override
    public UserQueue save(Long userId, Long concertScheduleId) {
        return userQueueJpaRepository.save(
            new UserQueue(userId, concertScheduleId)
        );
    }

    @Override
    public List<UserQueue> findStatusIsProgressBy(Long concertScheduleId) {
        return userQueueJpaRepository.findOrderByIdDescBy(concertScheduleId, UserQueueStatus.PROGRESS);
    }

    @Override
    public Integer updateStatusAndExpiredAt(UserQueueStatus status, LocalDateTime expiredAt, Long userId, Long concertScheduleId) {
        return userQueueJpaRepository.updateStatusAndExpiredAt(status, expiredAt, userId, concertScheduleId);
    }

    @Override
    public List<UserQueue> findAll() {
        return userQueueJpaRepository.findAll();
    }

}
