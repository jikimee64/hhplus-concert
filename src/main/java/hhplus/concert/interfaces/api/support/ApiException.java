package hhplus.concert.interfaces.api.support;

import hhplus.concert.interfaces.api.support.error.ErrorCode;
import lombok.Getter;
import org.springframework.boot.logging.LogLevel;

@Getter
public class ApiException extends RuntimeException {

    private final ErrorCode errorCode;

    private final LogLevel logLevel;

    private final Object data;

    public ApiException(ErrorCode errorCode, LogLevel logLevel) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.logLevel = logLevel;
        this.data = null;
    }

    public ApiException(ErrorCode errorCode, LogLevel logLevel, Object data) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.logLevel = logLevel;
        this.data = data;
    }

}
