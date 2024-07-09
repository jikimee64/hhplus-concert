package hhplus.concert.domain;

import java.util.List;

public interface ConcertRepository {
    List<ConcertSchedule> findAvailableReservation();
}
