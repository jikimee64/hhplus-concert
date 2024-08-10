package hhplus.concert.domain.userqueue;

import lombok.Getter;

@Getter
public class ActiveTokenDeleteEvent {

    private final String token;
    private Long messageOutboxId;

    public ActiveTokenDeleteEvent(String token) {
        this.token = token;
        this.messageOutboxId = 0L;
    }

    public void setMessageOutboxId(Long messageOutboxId) {
        this.messageOutboxId = messageOutboxId;
    }

}
