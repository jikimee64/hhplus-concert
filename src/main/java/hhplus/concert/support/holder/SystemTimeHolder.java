package hhplus.concert.support.holder;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class SystemTimeHolder implements TimeHolder {
    @Override
    public LocalDateTime currentDateTime() {
        return LocalDateTime.now();
    }
}
