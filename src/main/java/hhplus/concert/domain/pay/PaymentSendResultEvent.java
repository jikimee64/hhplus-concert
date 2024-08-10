package hhplus.concert.domain.pay;

import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PaymentSendResultEvent {

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

}
