package hhplus.concert.domain.concert;

import hhplus.concert.domain.concert.dto.SeatDto;
import hhplus.concert.domain.concert.dto.SeatQueryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ConcertFinder {

    private final ConcertRepository concertRepository;

    public List<ConcertSchedule> selectConcertScheduleBy(Long concertId, String status) {
        return concertRepository.findConcertScheduleBy(concertId, TotalSeatStatus.of(status));
    }

    /**
     * @return
     * 좌석 번호 1부터 50까지의 좌석 정보를 조회한다.
     * - 좌석 정보가 없는 경우, 좌석 상태는 'AVAILABLE'로 반환한다.
     * - 좌석 정보가 있는 경우, 좌석 상태는 'RESERVED'로 반환한다.
     */
    public List<SeatDto> selectSeatBy(Long concertScheduleId) {
        Integer totalSeat = concertRepository.findConcertSeat(concertScheduleId).get(0).totalSeat();
        List<SeatQueryDto> seatQueryDtos = concertRepository.findConcertSeat(concertScheduleId);
        Map<Integer, SeatQueryDto> seatQueryMap = toMap(seatQueryDtos);
        return generateSeatDtos(totalSeat, seatQueryMap);
    }

    public ConcertSchedule getConcertSchedule(Long concertScheduleId) {
        return concertRepository.findConcertSchedule(concertScheduleId);
    }

    /**
     * @return key: position, value: ConcertSeatQueryDto
     */
    private Map<Integer, SeatQueryDto> toMap(List<SeatQueryDto> seatQueryDtos) {
        return seatQueryDtos.stream()
                .collect(Collectors.toMap(
                        SeatQueryDto::position,
                        dto -> dto,
                        (existing, replacement) -> existing  // 키값 충돌시 첫 번째 값 유지
                ));
    }

    /**
     * TODO: 테이블에 좌석 정보가 없는 것은 id와 amount를 0으로 전달하여 프론트에서 처리?
     * 그럼 프론트에서 좌석 데이터를 관리해야 하는 것인가?
     */
    private List<SeatDto> generateSeatDtos(Integer totalSeat, Map<Integer, SeatQueryDto> seatQueryDtos) {
        return IntStream.rangeClosed(1, totalSeat)
                .mapToObj(i -> Optional.ofNullable(seatQueryDtos.get(i))
                        .map(seatQueryDto -> new SeatDto(
                                seatQueryDto.seatId(),
                                seatQueryDto.position(),
                                seatQueryDto.amount(),
                                seatQueryDto.status().name()
                        ))
                        .orElse(new SeatDto(0L, i, 0, SeatStatus.AVAILABLE.name())))
                .collect(Collectors.toList());
    }

}
