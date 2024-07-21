package hhplus.concert.domain.concert;

import hhplus.concert.IntegrationTest;
import hhplus.concert.infra.persistence.ConcertJpaRepository;
import hhplus.concert.infra.persistence.ConcertScheduleJpaRepository;
import hhplus.concert.infra.persistence.ReservationJpaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class ConcertManagerConcurrencyTest extends IntegrationTest {

    @Autowired
    private ConcertManager concertManager;

    @Autowired
    private ConcertJpaRepository concertJpaRepository;

    @Autowired
    private ConcertScheduleJpaRepository concertScheduleJpaRepository;

    @Autowired
    private ReservationJpaRepository reservationJpaRepository;

    @Test
    void 좌석_임시예약_동시성_테스트() throws InterruptedException {
        // given
        LocalDate concertOpenDate = LocalDate.of(2024, 1, 1);
        Long userId = 1L;
        Concert savedConcert = concertJpaRepository.save(
                new Concert(1L, "")
        );
        LocalDateTime now = LocalDateTime.now();
        ConcertSchedule savedConcertSchedule = concertScheduleJpaRepository.save(
                new ConcertSchedule(savedConcert, concertOpenDate, now.plusHours(1L), now.plusHours(2L), 50, TotalSeatStatus.AVAILABLE)
        );
        int seatPosition = 1;
        int seatAmount = 10000;

        int numberOfThreads = 100;
        ExecutorService service = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch latch = new CountDownLatch(numberOfThreads);

        for(int i = 0; i < numberOfThreads; i++) {
            service.execute(() -> {
                try{
                    // when
                    concertManager.reserveSeat(savedConcertSchedule.getId(), concertOpenDate, userId, seatPosition, seatAmount);
                }catch (Exception e) {
                    System.out.println(e.getMessage());
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();

        // then
        List<Reservation> byConcertScheduleId = reservationJpaRepository.findByConcertScheduleId(savedConcertSchedule.getId());
        assertThat(byConcertScheduleId).hasSize(1);
    }
}
