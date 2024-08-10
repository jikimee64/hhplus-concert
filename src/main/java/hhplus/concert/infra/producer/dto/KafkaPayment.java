package hhplus.concert.infra.producer.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class KafkaPayment {

    private final Long id;
    private final Long userId;
    private final String concertTitle;
    private final LocalDate concertOpenDate;
    private final LocalDateTime concertStartAt;
    private final LocalDateTime concertEndAt;
    private final Integer seatAmount;
    private final Integer seatPosition;
    private final LocalDateTime reservedAt;
    private final LocalDateTime paymentDate;

    public String getKey() {
        return String.valueOf(id);
    }

}
