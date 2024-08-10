package hhplus.concert.domain.pay;

import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PaymentSendResultEvent {

    private Long messageOutboxId;
    private Long paymentId;
    private Long userId;
    private String concertTitle;
    private LocalDate concertOpenDate;
    private LocalDateTime concertStartAt;
    private LocalDateTime concertEndAt;
    private Integer seatAmount;
    private Integer seatPosition;
    private LocalDateTime reservedAt;
    private LocalDateTime paymentedAt;

    public PaymentSendResultEvent(Long paymentId, Long userId, String concertTitle, LocalDate concertOpenDate, LocalDateTime concertStartAt,
        LocalDateTime concertEndAt, Integer seatAmount, Integer seatPosition, LocalDateTime reservedAt, LocalDateTime paymentedAt) {
        this.paymentId = paymentId;
        this.userId = userId;
        this.concertTitle = concertTitle;
        this.concertOpenDate = concertOpenDate;
        this.concertStartAt = concertStartAt;
        this.concertEndAt = concertEndAt;
        this.seatAmount = seatAmount;
        this.seatPosition = seatPosition;
        this.reservedAt = reservedAt;
        this.paymentedAt = paymentedAt;
        this.messageOutboxId = 0L;
    }

    public void setMessageOutboxId(Long messageOutboxId) {
        this.messageOutboxId = messageOutboxId;
    }
}
