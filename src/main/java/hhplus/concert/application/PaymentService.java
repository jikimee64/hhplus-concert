package hhplus.concert.application;

import hhplus.concert.application.dto.PayCommand;
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

    public Receipt pay(String token, PayCommand command) {
        return paymentManager.pay(token, command.userId(), command.concertScheduleId(), command.seatId(), command.concertOpenDate());
    }
}
