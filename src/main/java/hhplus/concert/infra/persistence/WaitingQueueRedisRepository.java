package hhplus.concert.infra.persistence;

import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class WaitingQueueRedisRepository {

    private final RedisTemplate<Long, String> waitingQueueRedisTemplate;

    public void add(Long concertScheduleId, String token) {
        long currentTimeMillis = System.currentTimeMillis();
        waitingQueueRedisTemplate.opsForZSet().add(concertScheduleId, token, currentTimeMillis);
    }

    // 0부터 시작하므로 1을 더해줌
    public Long rank(Long concertScheduleId, String token){
        Long rank = waitingQueueRedisTemplate.opsForZSet().rank(concertScheduleId, token);
        if(rank == null) {
            return 0L;
        }
        return rank + 1;
    }

    public Set<String> range(Long concertScheduleId, long start, long end) {
        return waitingQueueRedisTemplate.opsForZSet().range(concertScheduleId, start, end);
    }

    public Long delete(Long concertScheduleId, Set<String> tokens) {
        return waitingQueueRedisTemplate.opsForZSet().remove(concertScheduleId, tokens.toArray());
    }

}
