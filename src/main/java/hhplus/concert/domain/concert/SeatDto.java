package hhplus.concert.domain.concert;

public record SeatDto(
        Long id,
        Integer position,
        Integer amount,
        String status
) {
}
