## 대기열 설계를 rdb에서 redis로 변경

### 유저 대기열 토큰 발급
zadd 1 1 "one"
- zadd 명령어로 zset에 UUID를 추가해준다.
  - key: concertScheduleId
  - score: 타임스탬프
  - member UUID

### 유저 대기열 토큰 발급
- zrank 명령어로 zset에서 UUID의 순위를 확인한다.
  - 대기열 토큰에 없을 경우 활성화 토큰에 있는지 확인한다.
    - 활성화 토큰에도 없을 경우 유효하지 않은 토큰이므로 에외를 뱉는다.
    - 활성화 토큰에 있을 경우 0을 반환해 준다.
  - 대기열 토큰에 있을 경우 순위를 반환해 준다.
    - 첫 순위는 0을 반환해주기 때문에 +1을 더해준다

### 스케줄러로 N 명씩 활성화 토큰으로 변경 
- zrange 명령어로 대기열 토크에서 N명의 UUID를 가져온다.
- ZREM 명령어로 가져온 N명의 UUID를 대기열 토큰에서 삭제한다
- 활성화 토큰에 가져온 N명의 UUID를 key-value로 추가한다
  - key: "ACTIVE:{UUID}"
  - value: concertScheduleId
    - 사용하지 않는 값
  - ttl 5분

### active한 토큰인지 검사하는 인터셉터
- active 토큰에 없을 경우 예외를 뱉는다

### 토큰 삭제
- 결제 완료 후 zrem 명령어로 zset에서 UUID를 삭제한다.
