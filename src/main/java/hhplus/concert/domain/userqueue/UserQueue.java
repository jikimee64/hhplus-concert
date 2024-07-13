package hhplus.concert.domain.userqueue;

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

    private String token;

    @Enumerated(EnumType.STRING)
    private UserQueueStatus status;

    private LocalDateTime enteredAt;

    private LocalDateTime expiredAt;

    public UserQueue(Long userId, Long concertScheduleId, String token, UserQueueStatus status, LocalDateTime enteredAt, LocalDateTime expiredAt) {
        this.userId = userId;
        this.concertScheduleId = concertScheduleId;
        this.token = token;
        this.status = status;
        this.enteredAt = enteredAt;
        this.expiredAt = expiredAt;
    }

    public UserQueue(Long userId, Long concertScheduleId, String token, LocalDateTime enteredAt, UserQueueStatus status) {
        this(userId, concertScheduleId, token, status, enteredAt, null);
    }

    public UserQueue(Long userId, Long concertScheduleId, String token, UserQueueStatus status) {
        this(userId, concertScheduleId, token, status, LocalDateTime.now(), null);
    }

    public UserQueue(Long userId, Long concertScheduleId, UserQueueStatus status) {
        this(userId, concertScheduleId, "", status, LocalDateTime.now(), null);
    }

    public UserQueue(Long userId, Long concertScheduleId, String token) {
        this(userId, concertScheduleId, token, UserQueueStatus.WAITING, LocalDateTime.now(), null);
    }

    public boolean isExpired() {
        return this.status == UserQueueStatus.EXPIRED;
    }

    public void updateStatusDone(UserQueueStatus status) {
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getExpiredAt() {
        return expiredAt;
    }

    public UserQueueStatus getStatus() {
        return status;
    }

    public String getToken() {
        return token;
    }

    public LocalDateTime getEnteredAt() {
        return enteredAt;
    }
}
