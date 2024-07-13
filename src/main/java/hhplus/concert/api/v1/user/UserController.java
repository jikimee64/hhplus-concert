package hhplus.concert.api.v1.user;

import hhplus.concert.api.support.ApiResponse;
import hhplus.concert.api.v1.user.reqeust.ChargeCashRequest;
import hhplus.concert.api.v1.user.response.SelectCashResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@Tag(name = "유저 API", description = "유저 API")
@RestController
@RequestMapping("/v1/users")
public class UserController {

    @Operation(summary = "유저 잔액 충전", description = "유저 잔액을 충전한다.")
    @PostMapping("/{userId}/amount/charge")
    public ApiResponse<?> chargeCash(
            @Parameter(description = "유저 고유값")
            @PathVariable("userId") Long userId,
            @RequestBody ChargeCashRequest request
    ) {
        return ApiResponse.success();
    }

    @Operation(summary = "유저 잔액 조회", description = "유저 잔액을 조회한다.")
    @GetMapping("/{userId}/amount")
    public ApiResponse<SelectCashResponse> selectCash(
            @Parameter(description = "유저 고유값")
            @PathVariable("userId") Long userId
    ) {
        return ApiResponse.success(new SelectCashResponse(
                10000
        ));
    }
}
