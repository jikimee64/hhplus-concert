package hhplus.concert.application.external;

import hhplus.concert.domain.pay.PaymentSendResultEvent;
import hhplus.concert.infra.external.ExternalMockServerApi;
import hhplus.concert.infra.external.dto.PaymentSendExternalDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentSendResultEventFacade {

    private final ExternalMockServerApi externalMockServerApi;

    public void sendPaymentResult(PaymentSendResultEvent event) {
        externalMockServerApi.sendPaymentResult(
            new PaymentSendExternalDto(
                event.getUserId(),
                event.getConcertTitle(),
                event.getConcertOpenDate(),
                event.getConcertStartAt(),
                event.getConcertEndAt(),
                event.getSeatAmount(),
                event.getSeatPosition(),
                event.getReservedAt(),
                event.getPaymentedAt()
            )
        );
    }

}
