package hhplus.concert.infra.persistence;

import hhplus.concert.domain.UserQueue;
import hhplus.concert.domain.UserQueueStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserQueueJpaRepository extends JpaRepository<UserQueue, Long> {
    @Query("SELECT uq FROM UserQueue uq WHERE uq.concertScheduleId =:concertScheduleId AND uq.status =:status order by uq.id desc")
    List<UserQueue> findOrderByIdDescBy(
            @Param("concertScheduleId") Long concertScheduleId,
            @Param("status") UserQueueStatus status
    );

}
