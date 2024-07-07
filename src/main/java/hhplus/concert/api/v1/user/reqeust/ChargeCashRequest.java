package hhplus.concert.api.v1.user.reqeust;

import io.swagger.v3.oas.annotations.media.Schema;

public record ChargeCashRequest(
        @Schema(description = "충전 잔액")
        Integer amount
) {
}
