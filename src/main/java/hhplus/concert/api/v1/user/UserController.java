package hhplus.concert.api.v1.user;

import hhplus.concert.api.support.ApiResponse;
import hhplus.concert.api.v1.user.reqeust.ChargeCashRequest;
import hhplus.concert.api.v1.user.response.SelectCashResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/users")
public class UserController {

    @PostMapping("/{userId}/cash/charge")
    public ApiResponse<?> chargeCash(
            @PathVariable("userId") Long userId,
            @RequestBody ChargeCashRequest request
    ) {
        return ApiResponse.success();
    }

    @GetMapping("/{userId}/cash")
    public ApiResponse<SelectCashResponse> selectCash(
            @PathVariable("userId") Long userId
    ) {
        return ApiResponse.success(new SelectCashResponse(
                10000
        ));
    }
}
