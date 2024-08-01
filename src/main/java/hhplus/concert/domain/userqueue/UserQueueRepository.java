package hhplus.concert.domain.userqueue;

import java.time.LocalDateTime;
import java.util.List;

public interface UserQueueRepository {
    void save(Long userId, Long concertScheduleId, String token);
    UserQueue findByOrElseThrow(String token);
    Long waitingNumber(Long concertScheduleId, String token);
    List<UserQueue> findAllBy(UserQueueStatus status);
    List<UserQueue> findAllWaitingBy(Long concertScheduleId, Integer limitSize);
    Integer updateStatusByIds(List<Long> userQueueIds, UserQueueStatus status);
    Integer updateExpireConditionToken();
    List<UserQueue> findAll();
}
