package hhplus.concert.infra.persistence;

import java.util.Set;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class WaitingQueueRedisRepository {

    private final RedisTemplate<String, String> waitingQueueRedisTemplate;

    public WaitingQueueRedisRepository(@Qualifier("waitingQueueRedisTemplate") RedisTemplate<String, String> waitingQueueRedisTemplate) {
        this.waitingQueueRedisTemplate = waitingQueueRedisTemplate;
    }

    public void add(Long concertScheduleId, String token) {
        long currentTimeMillis = System.currentTimeMillis();
        waitingQueueRedisTemplate.opsForZSet().add(String.valueOf(concertScheduleId), token, currentTimeMillis);
    }

    // 0부터 시작하므로 1을 더해줌
    public Long rank(Long concertScheduleId, String token){
        Long rank = waitingQueueRedisTemplate.opsForZSet().rank(String.valueOf(concertScheduleId), token);
        if(rank == null) {
            return 0L;
        }
        return rank + 1;
    }

    public Set<String> range(Long concertScheduleId, long start, long end) {
        return waitingQueueRedisTemplate.opsForZSet().range(String.valueOf(concertScheduleId), start, end);
    }

    public Long delete(Long concertScheduleId, Set<String> tokens) {
        return waitingQueueRedisTemplate.opsForZSet().remove(String.valueOf(concertScheduleId), tokens.toArray());
    }

    public boolean exists(Long concertScheduleId) {
        return Boolean.TRUE.equals(waitingQueueRedisTemplate.hasKey(String.valueOf(concertScheduleId)));
    }

    public boolean existsWithMember(Long concertScheduleId, String token) {
        if (!exists(concertScheduleId)) {
            return false;
        }
        Double score = waitingQueueRedisTemplate.opsForZSet().score(String.valueOf(concertScheduleId), token);
        return score != null;
    }

    public void deleteAll() {
        Set<String> keys = waitingQueueRedisTemplate.keys("*");
        if (keys != null) {
            for (String key : keys) {
                waitingQueueRedisTemplate.delete(key);
            }
        }
    }

}
