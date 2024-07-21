package hhplus.concert.interfaces.api.v1.user;

import hhplus.concert.application.UserService;
import hhplus.concert.interfaces.api.support.ApiResponse;
import hhplus.concert.interfaces.api.v1.user.reqeust.ChargeCashRequest;
import hhplus.concert.interfaces.api.v1.user.response.SelectCashResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "유저 API", description = "유저 API")
@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(summary = "유저 잔액 충전", description = "유저 잔액을 충전한다.")
    @PostMapping("/{userId}/amount/charge")
    public ApiResponse<?> chargeCash(
            @Parameter(description = "유저 고유값")
            @PathVariable("userId") Long userId,
            @RequestBody ChargeCashRequest request
    ) {
        userService.chargeAmount(userId, request.amount());
        return ApiResponse.success();
    }

    @Operation(summary = "유저 잔액 조회", description = "유저 잔액을 조회한다.")
    @GetMapping("/{userId}/amount")
    public ApiResponse<SelectCashResponse> selectCash(
            @Parameter(description = "유저 고유값")
            @PathVariable("userId") Long userId
    ) {
        Integer userAmount = userService.selectAmount(userId);
        return ApiResponse.success(new SelectCashResponse(
                userAmount
        ));
    }
}
