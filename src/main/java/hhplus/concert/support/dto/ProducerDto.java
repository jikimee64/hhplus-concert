package hhplus.concert.support.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProducerDto implements KafkaDto {

    private String key;
    private String message;

    public String getMessageKey() {
        return key;
    }

}
