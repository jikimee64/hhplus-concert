package hhplus.concert.infra.external;

import hhplus.concert.infra.external.dto.PaymentSendExternalDto;
import org.springframework.stereotype.Component;

@Component
public class ExternalMockServerApi {

    public void sendPaymentResult(PaymentSendExternalDto paymentSendExternalDto) {
        System.out.println("sendPaymentResult");
    }

}
