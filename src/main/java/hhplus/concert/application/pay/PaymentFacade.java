package hhplus.concert.application.pay;

import hhplus.concert.application.pay.dto.PayCommand;
import hhplus.concert.application.pay.dto.ReceiptResult;
import hhplus.concert.domain.pay.dto.Receipt;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentFacade {

    private final hhplus.concert.domain.pay.PaymentService paymentService;

    public ReceiptResult pay(String token, PayCommand command) {
        Receipt receipt = paymentService.pay(token, command.userId(), command.concertScheduleId(), command.seatId(), command.concertOpenDate());
        return ReceiptResult.from(receipt);
    }
}
