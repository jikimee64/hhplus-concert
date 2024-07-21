package hhplus.concert.application;

import hhplus.concert.domain.pay.PaymentManager;
import hhplus.concert.domain.pay.Receipt;
import hhplus.concert.domain.userqueue.UserQueueManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentManager paymentManager;
    private final UserQueueManager userQueueManager;

    public Receipt pay(String token, Long userId, Long concertScheduleId, Long seatId, LocalDate concertOpenDate) {
        return paymentManager.pay(token, userId, concertScheduleId, seatId, concertOpenDate);
    }
}
