package hhplus.concert.infra.persistence;

import hhplus.concert.domain.concert.ConcertSchedule;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ConcertScheduleRedisRepository {

    private final RedisTemplate<String, Object> redisTemplate;

    public void saveConcertSchedules(Long concertId, List<ConcertSchedule> concertSchedules) {
        String key = "concert" + ":" + concertId + ":" + "concertSchedules";
        redisTemplate.opsForValue().set(key, concertSchedules);
        redisTemplate.expire(key, 24, TimeUnit.HOURS);
    }

    public List<ConcertSchedule> getConcertSchedules(Long concertId) {
        String key = "concert" + ":" + concertId + ":" + "concertSchedules";
        return (List<ConcertSchedule>) redisTemplate.opsForValue().get(key);
    }

    public void deleteAllConcertSchedules(Long concertId) {
        String pattern = "concert:" + concertId + ":concertSchedules";
        Set<String> keys = redisTemplate.keys(pattern);
        if (keys != null && !keys.isEmpty()) {
            redisTemplate.delete(keys);
        }
    }

    public void deleteConcertSchedules(Long concertId) {
        String key = "concert" + ":" + concertId + ":" + "concertSchedules";
        redisTemplate.delete(key);
    }

}
