## 시퀀스 다이어 그램

### 유저 대기열 토큰 기능

```mermaid
sequenceDiagram
    autonumber
    actor 사용자 as 사용자
    participant API as API
    participant 대기열 as 대기열
    사용자 ->> API: 토큰 생성 API 요청
    API ->> 대기열: 대기열 토큰 생성 요청
    대기열 ->> 대기열: 대기열 토큰 생성
    alt 대기열 토큰이 존재할 경우
        대기열 -->> API: 기존 대기열 토큰 반환
    else 대기열 토큰이 존재하지 않을 경우
        대기열 -->> API: 신규 대기열 토큰 반환
    end
    API -->> 사용자: 토큰 반환
    loop 대기열 순번 확인 API (5초마다 요청)
        사용자 ->> API: 토큰 적재하여 대기번호 확인 API 요청
        Note over 사용자, API: Authorization에 토큰적재
        API ->> 대기열: 대기번호 확인 요청
        대기열 ->> 대기열: 해당 콘서트의 대기열을 통과하여 활성화된 상태 모두 조회
        alt 대기열 제한 인원을 초과하지 않아 대기열에서 기다리지 않고 통과할 수 있는 경우
            대기열 -->> 대기열: 해당 대기열 토큰의 만료 시간을 현재시간 + 30분으로 업데이트
            대기열 -->> 대기열: 해당 대기열 토큰의 진행 상태를 PROGRESS로 업데이트
            대기열 -->> 사용자: 대기열을 기다리지 않아도 된다는 의미인 0 응답
        else 제한 인원이 대기열에 꽉차 대기열을 기다려야 할 경우
            Note over 대기열, 대기열: 대기열 상태값이 WAITING인 토큰의 전체 수 + 1
            대기열 -->> API: 몇번째 대기 순번인지 반환
            API -->> 사용자: 몇번째 대기 순번인지 반환
        end
    end
```

### 예약 가능 날짜 조회 API

```mermaid
sequenceDiagram
    autonumber
    actor 사용자 as 사용자
    participant API as API
    participant 날짜 as 날짜
    participant 대기열 as 대기열
    participant 대기열 토큰 만료 스케줄러 as 대기열 토큰 만료 스케줄러
    사용자 ->> API: 예약 가능한 날짜 조회 API 요청
    Note over 사용자, API: Authorization에 토큰적재
    API -> 날짜: 예약 가능한 날짜 조회
    날짜 ->> 대기열: 대기열 상태값 조회
    대기열 -->> 날짜: 대기열 상태값 조회
    alt
        날짜 -->> 사용자: 대기열 상태값이 EXPIRED일 경우 에러 응답
    else
        날짜 -->> API: 예약 가능한 날짜 조회 결과 반환
        API -->> 사용자: 예약 가능한 날짜 조회 결과 반환
    end

    rect rgba(0, 0, 255, .1)
        대기열 토큰 만료 스케줄러 --> 대기열 토큰 만료 스케줄러: 대기열의 토큰 상태가 PROGRSS인 토큰 중 만료 시간값이 5분이 지났을 경우 EXPIRED로 업데이트
    end
```

### 좌석 조회 API

```mermaid
sequenceDiagram
    autonumber
    actor 사용자 as 사용자
    participant API as API
    participant 좌석 as 좌석
    participant 대기열 as 대기열
    participant 대기열 토큰 만료 스케줄러 as 대기열 토큰 만료 스케줄러
    사용자 ->> API: 특정 날짜의 예약 가능한 좌석 조회 API 요청
    Note over 사용자, API: Authorization에 대기열 토큰적재
    API -> 좌석: 특정 날짜의 예약 가능한 좌석 조회
    좌석 ->> 대기열: 대기열 상태값 조회
    대기열 -->> 좌석: 대기열 상태값 반환
    alt
        좌석 -->> 사용자: 대기열 상태값이 EXPIRED일 경우 에러 응답
    else
        좌석 -->> API: 특정 날짜의 예약 가능한 좌석 조회 결과 반환
        API -->> 사용자: 특정 날짜의 예약 가능한 좌석 조회 결과 반환
    end
    rect rgba(0, 0, 255, .1)
        대기열 토큰 만료 스케줄러 --> 대기열 토큰 만료 스케줄러: 대기열의 토큰 상태가 PROGRSS인 토큰 중 만료 시간값이 5분이 지났을 경우 EXPIRED로 업데이트
    end

```

### 좌석 예약 요청 API

```mermaid
sequenceDiagram
    autonumber
    actor 사용자 as 사용자
    participant API as API
    participant 좌석 as 좌석
    participant 대기열 as 대기열
    participant 좌석 임시 배정 스케줄러 as 좌석 임시 배정 스케줄러
    participant 대기열 토큰 만료 스케줄러 as 대기열 토큰 만료 스케줄러
    사용자 ->> API: 날짜와 좌석 정보 입력하여 좌석 예약 API 요청
    Note over 사용자, API: Authorization에 토큰적재
    API ->> 좌석: 좌석 예약 요청
    좌석 ->> 대기열: 대기열 상태값 요청
    대기열 -->> 좌석: 대기열 상태값 반환
    alt
        좌석 -->> 사용자: 대기열 상태값이 EXPIRED일 경우 에러 응답
    else
        critical
            좌석 ->> 좌석: 특정 날짜에 좌석 임시 예약 요청
            좌석 -->> 사용자: 좌석이 찼을 경우 에러 반환
            좌석 -->> API: 좌석 임시 예약 성공 응답
            API -->> 사용자: 좌석 임시예약 성공 응답
        end

    end
    rect rgba(0, 0, 255, .1)
        좌석 임시 배정 스케줄러 ->> 좌석 임시 배정 스케줄러: 좌석 상태가 임시 예약 된 좌석 중 임시 예약 직후 5분 이내에 결제 완료되지 않았을 경우 임시 배정 해제
    end
    rect rgba(0, 0, 255, .1)
        대기열 토큰 만료 스케줄러 --> 대기열 토큰 만료 스케줄러: 대기열의 토큰 상태가 PROGRSS인 토큰 중 만료 시간값이 5분이 지났을 경우 EXPIRED로 업데이트
    end
```

### 잔액 충전 API

```mermaid
sequenceDiagram
    autonumber
    actor 사용자 as 사용자
    participant API as API
    participant 캐시 as 캐시
    사용자 ->> API: 잔액 충전 API 요청
    API ->> 캐시: 잔액 충전 요청
    캐시 ->> 캐시: 충전 금액 0이상 검사
    alt
        캐시 -->> 사용자: 충전 금액이 0 이하일 경우 에러 반환
    else
        캐시 -->> API: 충전 성공 응답
        API -->> 사용자: 충전 성공 응답
    end
```

### 잔액 조회 API

```mermaid
sequenceDiagram
    autonumber
    actor 사용자 as 사용자
    participant API as API
    participant 캐시 as 캐시
    사용자 ->> API: 잔액 조회 API 요청
    API ->> 캐시: 잔액 조회 요청
    캐시 -->> API: 잔액 조회 결과 반환
    API -->> 사용자: 잔액 조회 결과 반환
```

### 결제 API

```mermaid
sequenceDiagram
    autonumber
    participant 사용자 as 사용자
    participant API as API
    participant 결제 as 결제
    participant 대기열 as 대기열
    participant 캐시 as 캐시
    participant 콘서트 as 콘서트
    participant 대기열 토큰 만료 스케줄러 as 대기열 토큰 만료 스케줄러
    사용자 ->> API: 결제 요청
    API ->> 결제: 결제 요청
    결제 ->> 대기열: 대기열 상태값 조회
    대기열 -->> 결제: 대기열 상태값 반환
    alt
        결제 -->> 사용자: 대기열 상태값이 EXPIRED일 경우 에러 응답
    else
        결제 ->> 캐시: 결제 금액과 잔액 비교 요청
        캐시 ->> 캐시: 결제 금액과 잔액 비교
        캐시 -->> 사용자: 잔액 부족시 에러 응답
        캐시 ->> 캐시: 잔액에서 결제 금액 차감
        캐시 -->> 결제: 잔액에서 결제 금액 차감 성공 응답
        결제 ->> 콘서트: 좌석을 임시 배정에서 배정 상태로 변경
        opt 전체 좌석이 마감됬을 경우
            콘서트 ->> 콘서트: 좌석 전체 마감 여부를 마감으로 변경 
        end
        결제 ->> 대기열: 대기열 상태 값을 DONE으로 업데이트
        결제 -->> 결제: 결제내역 영수증 생성
        결제 -->> API: 결제내역 영수증 반환
        API -->> 사용자: 결제내역 영수증 반환
    end
    rect rgba(0, 0, 255, .1)
        대기열 토큰 만료 스케줄러 --> 대기열 토큰 만료 스케줄러: 대기열의 토큰 상태가 PROGRSS인 토큰 중 만료 시간값이 5분이 지났을 경우 EXPIRED로 업데이트
    end
```
