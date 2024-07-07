package hhplus.concert.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserQueue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private Long concertScheduleId;

    @Enumerated(EnumType.STRING)
    private UserQueueStatus status;

    private LocalDateTime enteredAt;

    private LocalDateTime expiredAt;

    public UserQueue(Long userId, Long concertScheduleId, UserQueueStatus status, LocalDateTime enteredAt, LocalDateTime expiredAt) {
        this.userId = userId;
        this.concertScheduleId = concertScheduleId;
        this.status = status;
        this.enteredAt = enteredAt;
        this.expiredAt = expiredAt;
    }

    public UserQueue(Long userId, Long concertScheduleId, UserQueueStatus status) {
        this(userId, concertScheduleId, status, LocalDateTime.now(), null);
    }

    public UserQueue(Long userId, Long concertScheduleId) {
        this(userId, concertScheduleId, UserQueueStatus.WAITING, LocalDateTime.now(), null);
    }

    public Long getId() {
        return id;
    }
}
