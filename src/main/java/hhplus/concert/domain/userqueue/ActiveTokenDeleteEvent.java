package hhplus.concert.domain.userqueue;

public class ActiveTokenDeleteEvent {
    private final String token;

    public ActiveTokenDeleteEvent(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

}
