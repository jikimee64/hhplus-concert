package hhplus.concert.interfaces.api.interceptor;

import hhplus.concert.domain.userqueue.UserQueueService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class UserQueueInterceptor implements HandlerInterceptor {

    private static final String QUEUE_TOKEN = "queueToken";

    private final UserQueueService userQueueService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String token = request.getHeader(QUEUE_TOKEN);
        userQueueService.validateTopExpiredBy(token);
        return true;
    }

}
