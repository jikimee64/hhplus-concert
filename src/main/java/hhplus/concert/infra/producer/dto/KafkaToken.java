package hhplus.concert.infra.producer.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class KafkaToken {

    private final String token;

}
