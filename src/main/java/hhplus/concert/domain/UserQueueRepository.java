package hhplus.concert.domain;

import java.time.LocalDateTime;
import java.util.List;

public interface UserQueueRepository {
    UserQueue save(Long userId, Long concertScheduleId);
    UserQueue findTopBy(Long userId, Long concertScheduleId);
    List<UserQueue> findStatusIsProgressBy(Long concertScheduleId);
    List<UserQueue> findStatusIsWaitingBy(Long concertScheduleId);
    Integer updateStatusAndExpiredAt(UserQueueStatus status, LocalDateTime expiredAt, Long userId, Long concertScheduleId);
    List<UserQueue> findAll();
}
