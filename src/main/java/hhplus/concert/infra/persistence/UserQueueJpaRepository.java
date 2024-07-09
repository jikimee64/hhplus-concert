package hhplus.concert.infra.persistence;

import hhplus.concert.domain.UserQueue;
import hhplus.concert.domain.UserQueueStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UserQueueJpaRepository extends JpaRepository<UserQueue, Long> {
    @Query("SELECT uq FROM UserQueue uq WHERE uq.concertScheduleId =:concertScheduleId AND uq.status =:status order by uq.id desc")
    List<UserQueue> findOrderByIdDescBy(
            @Param("concertScheduleId") Long concertScheduleId,
            @Param("status") UserQueueStatus status
    );

    Optional<UserQueue> findByToken(String token);

    Optional<UserQueue> findByUserIdAndConcertScheduleId(Long userId, Long concertScheduleId);

    @Modifying
    @Query("UPDATE UserQueue uq SET uq.expiredAt =:expiredAt, uq.status = :status WHERE uq.userId =:userId AND uq.concertScheduleId =:concertScheduleId")
    Integer updateStatusAndExpiredAt(
            @Param("status") UserQueueStatus status,
            @Param("expiredAt") LocalDateTime expiredAt,
            @Param("userId") Long userId,
            @Param("concertScheduleId") Long concertScheduleId
    );

}
