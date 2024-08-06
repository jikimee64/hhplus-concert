package hhplus.concert.application.event;

import hhplus.concert.domain.userqueue.UserQueueService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ActiveTokenDeleteEventFacade {

    private final UserQueueService userQueueService;

    public void deleteActiveToken(String token) {
        userQueueService.deleteActiveToken(token);
    }

}
