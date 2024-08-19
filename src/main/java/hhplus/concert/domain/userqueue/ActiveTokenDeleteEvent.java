package hhplus.concert.domain.userqueue;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ActiveTokenDeleteEvent {

    private String token;
    private Long messageOutboxId;

    public ActiveTokenDeleteEvent(String token) {
        this.token = token;
        this.messageOutboxId = 0L;
    }

    public void setMessageOutboxId(Long messageOutboxId) {
        this.messageOutboxId = messageOutboxId;
    }

}
