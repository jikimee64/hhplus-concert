package hhplus.concert.interfaces.api.support;

import hhplus.concert.interfaces.api.interceptor.PathMatcherInterceptor;
import hhplus.concert.interfaces.api.interceptor.PathMethod;
import hhplus.concert.interfaces.api.interceptor.UserQueueInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final UserQueueInterceptor userQueueInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor());
    }

    private HandlerInterceptor authInterceptor() {
        PathMatcherInterceptor interceptor = new PathMatcherInterceptor(userQueueInterceptor);

        return interceptor
                .includePathPattern("/v1/concerts/{concertScheduleId}/queue", PathMethod.GET)
                .includePathPattern("/v1/concerts/{concertId}/reservation/date", PathMethod.GET)
                .includePathPattern("/v1/concerts/{concertScheduleId}/reservation/seat", PathMethod.GET)
                .includePathPattern("/v1/concerts/{concertScheduleId}/reservation/seat", PathMethod.POST)
                .includePathPattern("/v1/concerts/{concertScheduleId}/purchase/seat/{seatId}", PathMethod.POST)
                .excludePathPattern("/**", PathMethod.OPTIONS)
                ;
    }

}
