package hhplus.concert.domain.userqueue;

import java.util.Set;

public interface UserQueueRepository {
    void save(Long userId, Long concertScheduleId, String token);
    Boolean isActiveToken(String token);
    void deleteActiveToken(String token);
    Long waitingNumber(Long concertScheduleId, String token);
    Set<String> getWaitingTokenRange(Long concertScheduleId, Long start, Long end);
    void deleteWaitingToken(Long concertScheduleId, Set<String> tokens);
    void addActiveToken(String token, Long concertScheduleId);
    Boolean existActiveToken(String token);
}
