package hhplus.concert.domain.userqueue;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UserQueueRepository {
    UserQueue save(Long userId, Long concertScheduleId, String token);
    UserQueue findByOrElseThrow(String token);
    Optional<UserQueue> findBy(Long userId, Long concertScheduleId);
    List<UserQueue> findStatusIsProgressBy(Long concertScheduleId);
    List<UserQueue> findAllBy(UserQueueStatus status);
    List<UserQueue> findAllWaitingBy(Long concertScheduleId, Integer limitSize);
    List<UserQueue> findStatusIsWaitingAndAlreadyEnteredBy(Long concertScheduleId, LocalDateTime enteredAt);
    Integer updateStatusByIds(List<Long> userQueueIds, UserQueueStatus status);
    Integer updateStatusAndExpiredAt(UserQueueStatus status, LocalDateTime expiredAt, String token);
    Integer updateExpireConditionToken();
    List<UserQueue> findAll();
}
