package hhplus.concert.interfaces.api.support.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    E001("001", "대기열 상태가 활성상태가 아닙니다."),
    E002("002", "좌석이 이미 예약 되어 있습니다."),
    E003("003", "충전 금액은 0이상 가능합니다."),
    E004("004", "유효하지 않은 대기열 토큰입니다."),
    E005("005", "결제 잔액이 부족합니다."),
    E006("006", "콘서트 개최 날짜가 일치하지 않습니다."),
    E404("404", "데이터를 조회할 수 없습니다."),
    E500("500", "알 수 없는 에러입니다. 관리자한테 문의해주세요.")
    ;

    private final String code;
    private final String message;

}
