package hhplus.concert.domain.concert.dto;

public record SeatDto(
        Long id,
        Integer position,
        Integer amount,
        String status
) {
}
