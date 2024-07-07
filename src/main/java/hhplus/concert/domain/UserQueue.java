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

}
