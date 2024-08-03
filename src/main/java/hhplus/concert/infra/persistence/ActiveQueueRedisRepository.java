package hhplus.concert.infra.persistence;

import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ActiveQueueRedisRepository {

    private final RedisTemplate<String, Object> activeQueueRedisTemplate;

    private final String keyPrefix = "ACTIVE:";

    public void add(String token, Long concertScheduleId) {
        String key = keyPrefix + token;
        activeQueueRedisTemplate.opsForValue().set(key, concertScheduleId, Duration.ofMinutes(5));
    }

    public boolean exists(String token) {
        String key = keyPrefix + token;
        return Boolean.TRUE.equals(activeQueueRedisTemplate.hasKey(key));
    }

    public void delete(String token) {
        String key = keyPrefix + token;
        activeQueueRedisTemplate.delete(key);
    }

}
