package hhplus.concert.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UserQueueRepository {
    UserQueue save(Long userId, Long concertScheduleId, String token);
    UserQueue findByOrElseThrow(String token);
    Optional<UserQueue> findBy(Long userId, Long concertScheduleId);
    List<UserQueue> findStatusIsProgressBy(Long concertScheduleId);
    List<UserQueue> findStatusIsWaitingAndAlreadyEnteredBy(Long concertScheduleId, LocalDateTime enteredAt);
    Integer updateStatusAndExpiredAt(UserQueueStatus status, LocalDateTime expiredAt, Long userId, Long concertScheduleId);
    Integer updateExpireConditionToken();
    List<UserQueue> findAll();
}
