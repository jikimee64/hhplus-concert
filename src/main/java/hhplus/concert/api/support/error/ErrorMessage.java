package hhplus.concert.api.support.error;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

import javax.lang.model.type.ErrorType;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Getter
@JsonInclude(NON_NULL)
public class ErrorMessage {

    private String code;
    private String message;
    private Object data;

    // 빈 객체로 반환하기 위한 기본 생성자 추가
    public ErrorMessage() {
    }

    public ErrorMessage(ErrorCode errorCode) {
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
        this.data = null;
    }

    public ErrorMessage(ErrorCode errorCode, Object data) {
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
        this.data = data;
    }
}