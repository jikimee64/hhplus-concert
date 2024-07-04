package hhplus.concert.api.support.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    E001("001", "An unexpected error has occurred.")
    ;

    private final String code;
    private final String message;

}
