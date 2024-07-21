package hhplus.concert.application.concert.dto;

import hhplus.concert.domain.concert.SeatDto;

import java.util.List;
import java.util.stream.Collectors;

public record SeatResult(
        Long id,
        Integer position,
        Integer amount,
        String status
) {
    public static List<SeatResult> from(List<SeatDto> seatDtos) {
        return seatDtos.stream()
                .map(SeatResult::from)
                .collect(Collectors.toList());
    }

    public static SeatResult from(SeatDto seatDto) {
        return new SeatResult(
                seatDto.id(),
                seatDto.position(),
                seatDto.amount(),
                seatDto.status()
        );
    }

}
