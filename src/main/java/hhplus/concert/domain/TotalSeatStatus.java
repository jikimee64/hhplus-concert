package hhplus.concert.domain;

import hhplus.concert.api.support.ApiException;
import hhplus.concert.api.support.error.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum TotalSeatStatus {
    SOLD_OUT("SOLD_OUT"),
    AVAILABLE("AVAILABLE");

    private final String status;

    public static TotalSeatStatus of(String status) {
        return Arrays.stream(TotalSeatStatus.values())
                .filter(totalSeatStatus -> totalSeatStatus.status.equals(status))
                .findFirst()
                .orElseThrow(() -> new ApiException(ErrorCode.E404, "TotalSeatStatus not found status: " + status));
    }

}
