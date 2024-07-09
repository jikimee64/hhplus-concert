package hhplus.concert.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ConcertFinder {

    private final ConcertRepository concertRepository;

    public List<ConcertSchedule> selectAvailableReservation() {
        return concertRepository.findAvailableReservation();
    }
}
