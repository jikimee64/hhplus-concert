package hhplus.concert.domain;

import java.util.List;

public interface UserQueueRepository {
    UserQueue save(Long userId, Long concertScheduleId);
    List<UserQueue> findAll();
}
