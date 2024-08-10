package hhplus.concert.domain.pay;

import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class PaymentSendResultEvent {

    private Long messageOutboxId;
    private final Long paymentId;
    private final Long userId;
    private final String concertTitle;
    private final LocalDate concertOpenDate;
    private final LocalDateTime concertStartAt;
    private final LocalDateTime concertEndAt;
    private final Integer seatAmount;
    private final Integer seatPosition;
    private final LocalDateTime reservedAt;
    private final LocalDateTime paymentedAt;

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
