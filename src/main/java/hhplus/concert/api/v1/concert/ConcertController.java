package hhplus.concert.api.v1.concert;

import hhplus.concert.api.support.ApiResponse;
import hhplus.concert.api.v1.concert.request.CreateQueueTokenRequest;
import hhplus.concert.api.v1.concert.request.PurchaseSeatRequest;
import hhplus.concert.api.v1.concert.request.ReserveSeatRequest;
import hhplus.concert.api.v1.concert.response.*;
import hhplus.concert.api.v1.concert.response.PurchaseSeatReceiptResponse.ReceiptResponse;
import hhplus.concert.api.v1.concert.response.SelectReservationSeatResponse.SeatResponse;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/v1/concerts")
public class ConcertController {

    @PostMapping("/{concertScheduleId}/queue-token")
    public ApiResponse<CreateQueueTokenResponse> createQueueToken(
            @PathVariable("concertScheduleId") Long concertScheduleId,
            @RequestBody CreateQueueTokenRequest request
    ) {
        return ApiResponse.success(new CreateQueueTokenResponse(
                "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c"
        ));
    }

    @GetMapping("/{concertScheduleId}/queue")
    public ApiResponse<SelectQueueResponse> selectQueue(
            @RequestHeader("Authorization") String queueToken,
            @PathVariable("concertScheduleId") Long concertScheduleId
    ) {
        return ApiResponse.success(new SelectQueueResponse(
                1
        ));
    }

    @GetMapping("/{concertScheduleId}/reservation/date")
    public ApiResponse<SelectReservationDateResponse> selectReservationDate(
            @RequestHeader("Authorization") String queueToken,
            @PathVariable("concertScheduleId") Long concertScheduleId,
            @RequestParam(value = "status", defaultValue = "ACTIVE") String status
    ) {
        return ApiResponse.success(new SelectReservationDateResponse(
                List.of(LocalDate.parse("2024-01-01"), LocalDate.parse("2024-01-02"))
        ));
    }

    @GetMapping("/{concertScheduleId}/reservation/seat")
    public ApiResponse<SelectReservationSeatResponse> selectReservationSeat(
            @RequestHeader("Authorization") String queueToken,
            @PathVariable("concertScheduleId") Long concertScheduleId,
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

    @PostMapping("/{concertScheduleId}/reservation/seat/{seatId}")
    public ApiResponse<?> reserveSeat(
            @RequestHeader("Authorization") String queueToken,
            @PathVariable("concertScheduleId") Long concertScheduleId,
            @PathVariable("seatId") Long seatId,
            @RequestBody ReserveSeatRequest request
    ) {
        return ApiResponse.success();
    }

    @PostMapping("/{concertScheduleId}/purchase/seat/{seatId}")
    public ApiResponse<PurchaseSeatReceiptResponse> purchaseSeat(
            @RequestHeader("Authorization") String queueToken,
            @PathVariable("concertScheduleId") Long concertScheduleId,
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
