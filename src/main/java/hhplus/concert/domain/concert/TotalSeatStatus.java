package hhplus.concert.domain.concert;

import hhplus.concert.interfaces.api.support.ApiException;
import hhplus.concert.interfaces.api.support.error.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.logging.LogLevel;

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
                .orElseThrow(() -> new ApiException(ErrorCode.E404, LogLevel.INFO, "TotalSeatStatus not found status: " + status));
    }

}
