# API Documentation

## API 목차

1. [개요](#개요)

2. [대기열 토큰](#대기열-토큰)

3. [Endpoints](#Endpoints)
    - [유저 대기열 토큰 발급 API](#유저-대기열-토큰-발급-API)
    - [유저 대기열 확인 API](#유저-대기열-확인-API)
    - [예약 날짜 조회 API](#예약-날짜-조회-API)
    - [좌석 예약 조회 API](#좌석-예약-조회-API)
    - [좌석 예약 요청 API](#좌석-예약-요청-API)
    - [잔액 충전 API](#잔액-충전-API)
    - [잔액 조회 API](#잔액-조회-API)
    - [결제 API](#결제-API)
4. [에러 응답 결과](#에러-응답-결과)

## 개요

콘서트 예약 서비스 API 문서

## 대기열 토큰

아래 API를 제외하고는 모든 API의 Authorization 헤더에 대기열 토큰을 포함해야 한다.

- 유저 대기열 토큰 발급 API
- 잔액 충전 API
- 잔액 조회 API

## Endpoints

### 유저 대기열 토큰 발급 API

유저는 대기열 토큰을 발급받는다.

- **URL:** `/v1/concerts/{concertScheduleId}/queue-token`
- **Method:** `POST`
- **URL Params:**
    - `concertScheduleId=[integer]` (required) 콘서트 스케줄 고유값
- **Request Body:**
    - **Code:** 200 OK
    - **Content:**
      ```json
      {
        "userId": 1
      }
      ```
- **Success Response:**
    - **Code:** 200 OK
    - **Content:**
    ```json
    {
      "result": "SUCCESS",
      "data": {
        "queueToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c"
      },
      "error": {}
    }
    ```

### 유저 대기열 확인 API

유저는 대기열 정보를 조회한다

- **URL:** `/v1/concerts/{concertScheduleId}/queue`
- **Method:** `GET`
- **Headers:**
    - `Authorization: Bearer QUEUE_TOKEN`
- **Success Response:**
    - **Code:** 200 OK
    - **Content:**
    ```json
    {
      "result": "SUCCESS",
      "data": {
        "waitingNumber": 1
      },
      "error": {}
    }
     ```
- **Error Response:**
    - **Code:** 200 OK
    - **Content:**
    ```json
      {
        "result": "FAIL",
        "data": {},
        "error": {
          "code": "004",
          "message": "유효하지 않은 대기열 토큰입니다."
        }
      }
    ```

### 예약 날짜 조회 API

콘서트의 예약 가능한 날짜를 조회한다.

- **URL:** `/v1/concerts/{concertId}/reservation/date?status=AVAILABLE`
- **Method:** `GET`
- **Headers:**
    - `Authorization: Bearer QUEUE_TOKEN`
- **URL Params:**
    - `concertId=[integer]` (required) 콘서트 고유값
- **Query Params:**
    - `status=[string]` (required) 예약 가능 상태값 (AVAILABLE(기본값), SOLD_OUT)
      - **Success Response:**
          - **Code:** 200 OK
          - **Content:**
          ```json
            {
                "result": "SUCCESS",
                "data": {
                  "concerts": [
                        {
                          "scheduleId": 1,
                          "openDate": "2024-01-01",
                          "startAt": "2024-01-01 00:00:00",
                          "endAt": "2024-01-01 01:00:00"
                        }
                  ]
                },
                "error": {}
            }
          ```
- **Error Response:**
    - **Code:** 200 OK
    - **Content:**
    ```json
      {
        "result": "FAIL",
        "data": {},
        "error": {
          "code": "004",
          "message": "유효하지 않은 대기열 토큰입니다."
        }
      }
    ```

    - **Code:** 200 OK
    - **Content:**
    ```json
      {
        "result": "FAIL",
        "data": {},
        "error": {
          "code": "001",
          "message": "대기열 상태가 활성상태가 아닙니다."
        }
      }
    ```

### 좌석 예약 조회 API

선택한 콘서트 스케줄의 좌석 정보를 조회한다.

- **URL:** `/v1/concerts/{concertScheduleId}/reservation/seat
- **Method:** `GET`
- **URL Params:**
    - `concertScheduleId=[integer]` (required) 콘서트 스케줄 고유값
- **Headers:**
    - `Authorization: Bearer QUEUE_TOKEN`
- **Success Response:**
    - **Code:** 200 OK
    - **Content:**
    ```json
      {
          "result": "SUCCESS",
          "data": {
            "seats":[
                 {          
                   "id": 1,
                   "position": 1,
                   "amount": 10000,
                   "status" : "AVAILABLE"
                 },
                 {          
                   "id": 2,
                   "position": 2,
                   "amount": 10000,
                   "status" : "TEMP_RESERVED"
                 },
                 {         
                   "id": 3, 
                   "position": 3,
                   "amount": 10000,
                   "status" : "RESERVED"
                 }
            ] 
          },
          "error": {}
      }
    ```
- **Error Response:**
    - **Code:** 200 OK
    - **Content:**
    ```json
      {
        "result": "FAIL",
        "data": {},
        "error": {
          "code": "004",
          "message": "유효하지 않은 대기열 토큰입니다."
        }
      }
    ```

    - **Code:** 200 OK
    - **Content:**
      ```json
        {
          "result": "FAIL",
          "data": {},
          "error": {
            "code": "001",
            "message": "대기열 상태가 활성상태가 아닙니다."
          }
        }
      ```

### 좌석 예약 요청 API

선택한 날짜의 좌석을 예약한다.

- **URL:** `/v1/concerts/{concertScheduleId}/reservation/seat/{seatId}`
- **Method:** `POST`
- **Headers:**
    - `Authorization: Bearer QUEUE_TOKEN`
- **URL Params:**
    - `concertScheduleId=[integer]` (required) 콘서트 스케줄 고유값
    - `seatId=[integer]` (required) 좌석 고유값
- **Request Body:**
    - **Content:**
      ```json
      {
        "userId": 1,
        "concertOpenDate": "2024-01-01"
      }
      ```
- **Success Response:**
    - **Code:** 200 OK
    - **Content:**
      ```json
        {
            "result": "SUCCESS",
            "data": {},
            "error": {}
        }
      ```
- **Error Response:**
    - **Code:** 400 Bad Request
    - **Content:**
    ```json
      {
        "result": "FAIL",
        "data": {},
        "error": {
          "code": "004",
          "message": "유효하지 않은 대기열 토큰입니다."
        }
      }
    ```

    - **Code:** 200 OK
    - **Content:**
      ```json
        {
          "result": "FAIL",
          "data": {},
          "error": {
            "code": "001",
            "message": "대기열 상태가 활성상태가 아닙니다."
          }
        }
      ```

  - **Code:** 200 OK
    - **Content:**
      ```json
        {
          "result": "FAIL",
          "data": {},
          "error": {
            "code": "002",
            "message": "좌석이 이미 예약 되어 있습니다."
          }
        }
      ```

  - **Code:** 200 OK
      - **Content:**
        ```json
          {
            "result": "FAIL",
            "data": {},
            "error": {
              "code": "006",
              "message": "콘서트 개최 날짜가 일치하지 않습니다."
            }
          }
        ```

### 잔액 충전 API

- 잔액을 충전한다

- **URL:** `/v1/users/{userId}/amount/charge`
- **Method:** `POST`
- **URL Params:**
    - `userId=[integer]` (required) 유저 고유값
- **Request Body:**
    - **Content:**
      ```json
      {
        "amount": 10000
      }
      ```
- **Success Response:**
    - **Code:** 200 OK
    - **Content:**
      ```json
        {
            "result": "SUCCESS",
            "data": {},
            "error": {}
        }
      ```

### 잔액 조회 API

- 잔액을 조회한다

- **URL:** `/v1/users/{userId}/amount`
- **Method:** `GET`
- **URL Params:**
    - `userId=[integer]` (required) 유저 고유값
- **Success Response:**
    - **Code:** 200 OK
    - **Content:**
      ```json
        {
            "result": "SUCCESS",
            "data": {
              "amount": 10000
            },
            "error": {}
        }
      ```

### 결제 API

- 좌석을 결제 한다

- **URL:** `/v1/concerts/{concertScheduleId}/purchase/seat/{seatId}`
- **Method:** `POST`
- **Headers:**
    - `Authorization: Bearer QUEUE_TOKEN`
- **URL Params:**
    - `concertScheduleId=[integer]` (required) 콘서트 스케줄 고유값
    - `seatId=[integer]` (required) 좌석 고유값
- **Request Body:**
    - **Code:** 200 OK
    - **Content:**
      ```json
      {
        "concertOpenDate": "2024-01-01",
        "purchaseAmount": 10000
      }
      ```
- **Success Response:**
    - **Code:** 200 OK
    - **Content:**
      ```json
        {
            "result": "SUCCESS",
            "data": {
              "receipt": {
                "purchaseNumber": 1,
                "concertName": "콘서트 명",
                "concertOpenDate": "2024-01-01",
                "seatPosition": 10,
                "purchaseAmount": 10000,
                "purchaseDate": "2024-01-01 00:00:00"
              }
            },
            "error": {}
        }
      ```
- **Error Response:**
    - **Code:** 200 OK
    - **Content:**
    ```json
      {
        "result": "FAIL",
        "data": {},
        "error": {
          "code": "004",
          "message": "유효하지 않은 대기열 토큰입니다."
        }
      }
    ```

    - **Code:** 200 OK
    - **Content:**
      ```json
        {
          "result": "FAIL",
          "data": {},
          "error": {
            "code": "001",
            "message": "대기열 상태가 활성상태가 아닙니다."
          }
        }
      ```

    - **Code:** 200 OK
    - **Content:**
      ```json
        {
          "result": "FAIL",
          "data": {},
          "error": {
            "code": "005",
            "message": "결제 잔액이 부족합니다."
          }
        }
      ```

## 에러 응답 결과

HTTP Status 별 에러 응답

- **500 Internal Server Error**
    ```json
      {
        "result": "FAIL",
        "data": {},
        "error": {
          "code": "500",
          "message": "알 수 없는 에러입니다. 관리자한테 문의해주세요."
        }
      }
    ```

- **400 Bad Request**
    ```json
      {
        "result": "FAIL",
        "data": {},
        "error": {
          "code": "400",
          "message": "{요청값 유효성 에러 메시지}"
        }
      }
    ```