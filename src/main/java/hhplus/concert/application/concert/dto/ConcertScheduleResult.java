package hhplus.concert.application.concert.dto;

import hhplus.concert.domain.concert.ConcertSchedule;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public record ConcertScheduleResult(
        Long scheduleId,
        LocalDate openDate,
        LocalDateTime startTime,
        LocalDateTime endTime
) {

    public static List<ConcertScheduleResult> from(List<ConcertSchedule> concertSchedules) {
        return concertSchedules.stream()
                .map(ConcertScheduleResult::from)
                .collect(Collectors.toList());
    }

    public static ConcertScheduleResult from(ConcertSchedule concertSchedule) {
        return new ConcertScheduleResult(
                concertSchedule.getId(),
                concertSchedule.getOpenDate(),
                concertSchedule.getStartAt(),
                concertSchedule.getEndAt()
        );
    }
}
