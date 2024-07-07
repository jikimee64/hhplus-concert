package hhplus.concert.infra.persistencen;

import hhplus.concert.domain.UserQueue;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserQueueJpaRepository extends JpaRepository<UserQueue, Long> {
}
