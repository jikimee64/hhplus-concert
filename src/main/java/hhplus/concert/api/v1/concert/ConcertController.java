package hhplus.concert.api.v1.concert;

import hhplus.concert.api.support.ApiResponse;
import hhplus.concert.api.v1.concert.request.CreateQueueTokenRequest;
import hhplus.concert.api.v1.concert.request.PurchaseSeatRequest;
import hhplus.concert.api.v1.concert.request.ReserveSeatRequest;
import hhplus.concert.api.v1.concert.response.*;
import hhplus.concert.api.v1.concert.response.PurchaseSeatReceiptResponse.ReceiptResponse;
import hhplus.concert.api.v1.concert.response.SelectReservationSeatResponse.SeatResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Tag(name = "콘서트 API", description = "콘서트 API")
@RestController
@RequestMapping("/v1/concerts")
public class ConcertController {

    @Operation(summary = "유저 대기열 토큰 발급", description = "유저가 대기열 진입시 토큰을 발급한다.")
    @PostMapping("/{concertScheduleId}/queue-token")
    public ApiResponse<CreateQueueTokenResponse> createQueueToken(
            @Parameter(description = "콘서트 스케줄 고유값")
            @PathVariable("concertScheduleId") Long concertScheduleId,
            @RequestBody CreateQueueTokenRequest request
    ) {
        return ApiResponse.success(new CreateQueueTokenResponse(
                "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c"
        ));
    }

    @Operation(summary = "유저 대기열 조회", description = "유저가 대기열 상태값을 조회한다.")
    @GetMapping("/{concertScheduleId}/queue")
    public ApiResponse<SelectQueueResponse> selectQueue(
            @Parameter(description = "대기열 토큰")
            @RequestHeader("Authorization") String queueToken,
            @Parameter(description = "콘서트 스케줄 고유값")
            @PathVariable("concertScheduleId") Long concertScheduleId
    ) {
        return ApiResponse.success(new SelectQueueResponse(
                1
        ));
    }

    @Operation(summary = "예약 가능 날짜 조회", description = "예약 가능 날짜를 조회한다")
    @GetMapping("/{concertScheduleId}/reservation/date")
    public ApiResponse<SelectReservationDateResponse> selectReservationDate(
            @Parameter(description = "대기열 토큰")
            @RequestHeader("Authorization") String queueToken,
            @Parameter(description = "콘서트 스케줄 고유값")
            @PathVariable("concertScheduleId") Long concertScheduleId,
            @Parameter(description = "예약 가능 상태")
            @RequestParam(value = "status", defaultValue = "ACTIVE") String status
    ) {
        return ApiResponse.success(new SelectReservationDateResponse(
                List.of(LocalDate.parse("2024-01-01"), LocalDate.parse("2024-01-02"))
        ));
    }

    @Operation(summary = "좌석 조회", description = "특정 날짜의 좌석을 조회한다")
    @GetMapping("/{concertScheduleId}/reservation/seat")
    public ApiResponse<SelectReservationSeatResponse> selectReservationSeat(
            @Parameter(description = "대기열 토큰")
            @RequestHeader("Authorization") String queueToken,
            @Parameter(description = "콘서트 스케줄 고유값")
            @PathVariable("concertScheduleId") Long concertScheduleId,
            @Parameter(description = "콘서트 오픈 날짜(2024-01-01)")
            @RequestParam(value = "concertOpenDate") LocalDate concertOpenDate
    ) {
        return ApiResponse.success(new SelectReservationSeatResponse(
                List.of(
                        new SeatResponse(
                                1L,
                                1,
                                10000,
                                "AVAILABLE"
                        ),
                        new SeatResponse(
                                2L,
                                2,
                                10000,
                                "TEMP_RESERVED"
                        ),
                        new SeatResponse(
                                3L,
                                3,
                                10000,
                                "RESERVED"
                        )
                )
        ));
    }

    @Operation(summary = "좌석을 예약한다", description = "좌석을 예약한다")
    @PostMapping("/{concertScheduleId}/reservation/seat/{seatId}")
    public ApiResponse<?> reserveSeat(
            @Parameter(description = "대기열 토큰")
            @RequestHeader("Authorization") String queueToken,
            @Parameter(description = "콘서트 스케줄 고유값")
            @PathVariable("concertScheduleId") Long concertScheduleId,
            @Parameter(description = "좌석 고유값")
            @PathVariable("seatId") Long seatId,
            @RequestBody ReserveSeatRequest request
    ) {
        return ApiResponse.success();
    }

    @Operation(summary = "좌석을 구매한다", description = "좌석을 구매한다")
    @PostMapping("/{concertScheduleId}/purchase/seat/{seatId}")
    public ApiResponse<PurchaseSeatReceiptResponse> purchaseSeat(
            @Parameter(description = "대기열 토큰")
            @RequestHeader("Authorization") String queueToken,
            @Parameter(description = "콘서트 스케줄 고유값")
            @PathVariable("concertScheduleId") Long concertScheduleId,
            @Parameter(description = "좌석 고유값")
            @PathVariable("seatId") Long seatId,
            @RequestBody PurchaseSeatRequest request
    ) {
        return ApiResponse.success(new PurchaseSeatReceiptResponse(
                new ReceiptResponse(
                        1L,
                        "콘서트 명",
                        LocalDate.of(2024,1,1),
                        10,
                        10000,
                        LocalDateTime.of(2024,1,1,0,0,0)
                )
        ));
    }

}
