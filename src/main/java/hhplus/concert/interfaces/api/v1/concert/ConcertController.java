package hhplus.concert.interfaces.api.v1.concert;

import hhplus.concert.application.concert.ConcertService;
import hhplus.concert.application.pay.PaymentService;
import hhplus.concert.application.userqueue.UserQueueService;
import hhplus.concert.application.concert.dto.ConcertScheduleResult;
import hhplus.concert.application.pay.dto.PayCommand;
import hhplus.concert.application.concert.dto.ReservationSeatCommand;
import hhplus.concert.application.concert.dto.SeatResult;
import hhplus.concert.domain.pay.Receipt;
import hhplus.concert.interfaces.api.support.ApiResponse;
import hhplus.concert.interfaces.api.v1.concert.request.CreateQueueTokenRequest;
import hhplus.concert.interfaces.api.v1.concert.request.PurchaseSeatRequest;
import hhplus.concert.interfaces.api.v1.concert.request.ReserveSeatRequest;
import hhplus.concert.interfaces.api.v1.concert.response.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "콘서트 API", description = "콘서트 API")
@RestController
@RequestMapping("/v1/concerts")
@RequiredArgsConstructor
public class ConcertController {

    private final ConcertService concertService;
    private final UserQueueService userQueueService;
    private final PaymentService paymentService;

    @Operation(summary = "유저 대기열 토큰 발급", description = "유저가 대기열 진입시 토큰을 발급한다.")
    @PostMapping("/{concertScheduleId}/queue-token")
    public ApiResponse<CreateQueueTokenResponse> createQueueToken(
            @Parameter(description = "콘서트 스케줄 고유값")
            @PathVariable("concertScheduleId") Long concertScheduleId,
            @RequestBody CreateQueueTokenRequest request
    ) {
        String queueToken = userQueueService.selectToken(concertScheduleId, request.userId());
        return ApiResponse.success(new CreateQueueTokenResponse(
                queueToken
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
        Integer waitingNumber = userQueueService.selectWaitingNumber(queueToken, concertScheduleId);
        return ApiResponse.success(new SelectQueueResponse(
                waitingNumber
        ));
    }

    @Operation(summary = "예약 가능 날짜 조회", description = "예약 가능 날짜를 조회한다")
    @GetMapping("/{concertId}/reservation/date")
    public ApiResponse<List<ConcertScheduleResult>> selectReservationDate(
            @Parameter(description = "대기열 토큰")
            @RequestHeader("Authorization") String queueToken,
            @Parameter(description = "콘서트 스케줄 고유값")
            @PathVariable("concertId") Long concertId,
            @Parameter(description = "예약 가능 상태")
            @RequestParam(value = "status", defaultValue = "ACTIVE") String status
    ) {
        List<ConcertScheduleResult> concertScheduleResults = concertService.selectConcertSchedule(queueToken, concertId, status);
        return ApiResponse.success(concertScheduleResults);
    }

    @Operation(summary = "좌석 조회", description = "특정 날짜의 좌석을 조회한다")
    @GetMapping("/{concertScheduleId}/reservation/seat")
    public ApiResponse<List<SeatResult>> selectReservationSeat(
            @Parameter(description = "대기열 토큰")
            @RequestHeader("Authorization") String queueToken,
            @Parameter(description = "콘서트 스케줄 고유값")
            @PathVariable("concertScheduleId") Long concertScheduleId
    ) {
        List<SeatResult> seatResults = concertService.selectSeat(queueToken, concertScheduleId);
        return ApiResponse.success(seatResults);
    }

    @Operation(summary = "좌석을 예약한다", description = "좌석을 예약한다")
    @PostMapping("/{concertScheduleId}/reservation/seat")
    public ApiResponse<?> reserveSeat(
            @Parameter(description = "대기열 토큰")
            @RequestHeader("Authorization") String queueToken,
            @Parameter(description = "콘서트 스케줄 고유값")
            @PathVariable("concertScheduleId") Long concertScheduleId,
            @RequestBody ReserveSeatRequest request
    ) {
        concertService.reserveSeat(
                queueToken,
                new ReservationSeatCommand(
                        concertScheduleId,
                        request.userId(),
                        request.seatPosition(),
                        request.seatAmount(),
                        request.concertOpenDate()

                )
        );
        return ApiResponse.success();
    }

    @Operation(summary = "좌석을 결제한다", description = "좌석을 결제한다")
    @PostMapping("/{concertScheduleId}/purchase/seat/{seatId}")
    public ApiResponse<Receipt> purchaseSeat(
            @Parameter(description = "대기열 토큰")
            @RequestHeader("Authorization") String queueToken,
            @Parameter(description = "콘서트 스케줄 고유값")
            @PathVariable("concertScheduleId") Long concertScheduleId,
            @Parameter(description = "좌석 고유값")
            @PathVariable("seatId") Long seatId,
            @RequestBody PurchaseSeatRequest request
    ) {
        Receipt receipt = paymentService.pay(
                queueToken,
                new PayCommand(
                        request.userId(),
                        concertScheduleId,
                        seatId,
                        request.concertOpenDate()
                )
        );
        return ApiResponse.success(receipt);
    }

}
