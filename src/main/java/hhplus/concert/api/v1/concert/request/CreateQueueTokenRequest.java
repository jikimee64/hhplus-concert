package hhplus.concert.api.v1.concert.request;

import io.swagger.v3.oas.annotations.media.Schema;

public record CreateQueueTokenRequest(
        @Schema(description = "유저 고유값")
        Long userId
) {
}
