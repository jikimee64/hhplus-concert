package hhplus.concert.infra.persistencen;

import hhplus.concert.domain.UserQueue;
import hhplus.concert.domain.UserQueueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserQueueRepositoryImpl implements UserQueueRepository {

    private final UserQueueJpaRepository userQueueJpaRepository;

    @Override
    public UserQueue save(Long userId, Long concertScheduleId) {
        return userQueueJpaRepository.save(
            new UserQueue(userId, concertScheduleId)
        );
    }

    @Override
    public List<UserQueue> findAll() {
        return userQueueJpaRepository.findAll();
    }

}
