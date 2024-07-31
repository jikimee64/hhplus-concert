### 어플리케이션 조회 API
- 유저 대기열 조회
- 유저 잔액 조회
- 예약 가능 날짜 조회
- 특정 날짜의 좌석 조회

### 캐싱 적용 전 API 분석
- 유저 대기열 조회
  - 레디스의 SortedSet, Set 자료구조로 구현할 것이기 때문에 캐싱을 필요하지 않다.  
- 유저 잔액 조회
  - 유저의 개인 정보이고, 잔액 같은 경우 정합성이 중요하기 때문에 캐싱으로 적합하지 않다.
- 특정 날짜의 좌석 조회
  - 좌석 상태는 비즈니스 특성상 실시간으로 보여줘야 하기 때문에 캐싱에 적합하지 않다.
  - 또한, 좌석 예약이 몰릴 경우 좌석의 상태값이 자주 변해 캐시 데이터도 자주 변경되어야 한다.
  - 쿼리가 복잡하더라도 실시간 데이터를 보여주는 것이 중요하다. 쿼리 부하는 캐싱으로는 해결하기엔 적합하지 않다.
  ```sql
    SELECT
    concert_seat.id AS concert_seat_id,
    concert_schedule.total_seat AS total_seat,
    concert_seat.position AS seat_position,
    concert_seat.amount AS seat_amount,
    reservation.status AS reservation_status
    FROM concert_schedule
    LEFT JOIN concert_seat ON concert_schedule.id = concert_seat.concert_schedule_id AND concert_seat.id IS NOT NULL
    LEFT JOIN reservation ON concert_seat.id = reservation.seat_id AND reservation.id IS NOT NULL
    WHERE concert_schedule.id = {concert_schedule.id}
  ```
- 예약 가능한 날짜 조회
  - 쿼리가 간단하다.
  ```sql
    SELECT *
    FROM concert_schedule
    WHERE concert_schedule.concert_id = {concert_id}
    AND concert_schedule.total_seat_status = {'AVAILABLE' | 'SOLD_OUT'}
  ```

### 캐싱 적용 선택 API
- 예약 가능한 날짜 조회

### 캐싱 전략
- 캐시 데이터를 먼저 찾고 없으면 DB에서 조회한 뒤 결과를 반환해 주고 레디스에 쿼리 결과를 새롭게 캐싱한다
- 콘서트 스케줄의 전체 좌석 상태가 변경되는 경우 캐시를 지운다.(evict)
  - 좌석 예약시 마지막 좌석을 에약 하여 좌석 상태가 만료로 변경되는 경우
  - 스케줄러로 좌석 임시예약 해제시 좌석이 모두 찬 상태에서 좌석 임시예약이 해제되었을 경우 좌석 상태를 이용가능으로 변경하는 경우
