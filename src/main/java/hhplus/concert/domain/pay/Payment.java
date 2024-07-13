package hhplus.concert.domain.pay;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private Long reservationId;

    private Integer price;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    private LocalDateTime createdAt;

    public Payment(Long id, Long userId, Long reservationId, Integer price, PaymentStatus status, LocalDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.reservationId = reservationId;
        this.price = price;
        this.status = status;
        this.createdAt = createdAt;
    }

    public Payment(Long userId, Long reservationId, Integer price) {
        this.userId = userId;
        this.reservationId = reservationId;
        this.price = price;
        this.status = PaymentStatus.PROGRESS;
        this.createdAt = LocalDateTime.now();
    }

    public Payment(Long userId, Long reservationId, PaymentStatus status) {
        this(null, userId, reservationId, null, status, LocalDateTime.now());
    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
