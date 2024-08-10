package hhplus.concert.interfaces.event;

import hhplus.concert.interfaces.api.support.ApiException;
import hhplus.concert.interfaces.api.support.error.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.logging.LogLevel;

@Slf4j
public abstract class RetryableEventListener<T> {

    public void handleEventWithRetry(T event, int maxAttempts, long backoffDelay) {
        int attempts = 0;
        while (attempts < maxAttempts) {
            try {
                attempts++;
                handleEvent(event);
                return; // 성공 시 메서드 종료
            } catch (Exception e) {
                log.error("이벤트 실패 > 재시도 수행, attempt {}/{}", attempts, maxAttempts, e);
                if (attempts >= maxAttempts) {
                    log.error("재시도 횟수 초과 {}/{}", attempts, maxAttempts, e);
                    throw new ApiException(
                        ErrorCode.E998,
                        LogLevel.INFO,
                        "attempt {} " + attempts + "/{}" + maxAttempts
                    );
                }
                try {
                    Thread.sleep(backoffDelay); // 백오프 적용
                } catch (InterruptedException interruptedException) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException("Retry interrupted", interruptedException);
                }
            }
        }
    }

    protected abstract void handleEvent(T event) throws Exception;
}