package hhplus.concert.api.support;


import hhplus.concert.api.support.error.ErrorCode;
import hhplus.concert.api.support.error.ErrorMessage;
import lombok.Getter;

import java.util.HashMap;

import static hhplus.concert.api.support.ResultType.FAIL;
import static hhplus.concert.api.support.ResultType.SUCCESS;

@Getter
public class ApiResponse<T> {
    private final ResultType result;

    private final T data;

    private final ErrorMessage error;

    private ApiResponse(ResultType result, T data, ErrorMessage error) {
        this.result = result;
        this.data = data;
        this.error = error;
    }

    public static ApiResponse<?> success() {
        return new ApiResponse<>(SUCCESS, new HashMap<>(), new ErrorMessage());
    }

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(SUCCESS, data, new ErrorMessage());
    }

    public static ApiResponse<?> error(ErrorCode error) {
        return new ApiResponse<>(FAIL, new HashMap<>(), new ErrorMessage(error));
    }

    public static ApiResponse<?> error(ErrorCode error, Object errorData) {
        return new ApiResponse<>(FAIL, new HashMap<>(), new ErrorMessage(error, errorData));
    }

}

