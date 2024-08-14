package hhplus.concert.infra.producer.kafka.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PublisherPaymentMessage {

    private Long id;
    private Long userId;
    private String concertTitle;
    private LocalDate concertOpenDate;
    private LocalDateTime concertStartAt;
    private LocalDateTime concertEndAt;
    private Integer seatAmount;
    private Integer seatPosition;
    private LocalDateTime reservedAt;
    private LocalDateTime paymentDate;

    public String getKey() {
        return String.valueOf(id);
    }

}
