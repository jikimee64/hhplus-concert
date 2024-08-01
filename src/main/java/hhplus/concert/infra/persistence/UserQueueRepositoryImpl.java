package hhplus.concert.infra.persistence;

import hhplus.concert.domain.userqueue.UserQueueRepository;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserQueueRepositoryImpl implements UserQueueRepository {

    private final WaitingQueueRedisRepository waitingQueueRedisRepository;
    private final ActiveQueueRedisRepository activeQueueRedisRepository;

    @Override
    public void save(Long userId, Long concertScheduleId, String token) {
        waitingQueueRedisRepository.add(concertScheduleId, token);
    }

    @Override
    public Boolean isActiveToken(String token) {
        return activeQueueRedisRepository.exists(token);
    }

    @Override
    public void deleteActiveToken(String token) {
        activeQueueRedisRepository.delete(token);
    }

    @Override
    public Long waitingNumber(Long concertScheduleId, String token) {
        return waitingQueueRedisRepository.rank(concertScheduleId, token);
    }

    @Override
    public Set<String> getWaitingTokenRange(Long concertScheduleId, Long start, Long end) {
        return waitingQueueRedisRepository.range(concertScheduleId, start, end);
    }

    @Override
    public void deleteWaitingToken(Long concertScheduleId, Set<String> tokens) {
        waitingQueueRedisRepository.delete(concertScheduleId, tokens);
    }

    @Override
    public void addActiveToken(String token, Long concertScheduleId) {
        activeQueueRedisRepository.add(token, concertScheduleId);
    }

}
