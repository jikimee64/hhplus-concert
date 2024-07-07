package hhplus.concert.domain;

public interface UserQueueRepository {
    UserQueue save(Long userId, Long concertScheduleId);
}
