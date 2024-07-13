package hhplus.concert.domain;

public record SeatDto(
        Long id,
        Integer position,
        Integer amount,
        String status
) {
}
