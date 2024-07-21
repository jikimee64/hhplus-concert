package hhplus.concert.application.pay;

import hhplus.concert.application.pay.dto.PayCommand;
import hhplus.concert.domain.pay.PaymentManager;
import hhplus.concert.domain.pay.Receipt;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentManager paymentManager;

    public Receipt pay(String token, PayCommand command) {
        return paymentManager.pay(token, command.userId(), command.concertScheduleId(), command.seatId(), command.concertOpenDate());
    }
}
