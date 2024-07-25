package hhplus.concert.domain.concert;

import hhplus.concert.infra.persistence.ConcertJpaRepository;
import hhplus.concert.infra.persistence.ConcertScheduleJpaRepository;
import hhplus.concert.infra.persistence.ConcertSeatJpaRepository;
import hhplus.concert.infra.persistence.ReservationJpaRepository;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.EntityManager;
import org.checkerframework.dataflow.qual.AssertMethod;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@ActiveProfiles("test")
@SpringBootTest
public class ConcertManagerConcurrencyTest {

    @Autowired
    private ConcertManager concertManager;

    @Autowired
    private ConcertJpaRepository concertJpaRepository;

    @Autowired
    private ConcertScheduleJpaRepository concertScheduleJpaRepository;

    @Autowired
    private ConcertSeatJpaRepository concertSeatJpaRepository;

    @Autowired
    private ReservationJpaRepository reservationJpaRepository;

    private Long userId = 1L;
    private ConcertSchedule concertSchedule;
    private ConcertSeat concertSeat;

    @BeforeEach
    void setUp() {
        LocalDate concertOpenDate = LocalDate.of(2024, 1, 1);
        Concert savedConcert = concertJpaRepository.save(
                new Concert(1L, "")
        );
        LocalDateTime now = LocalDateTime.now();
        concertSchedule = concertScheduleJpaRepository.save(
                new ConcertSchedule(savedConcert, concertOpenDate, now.plusHours(1L), now.plusHours(2L), 50, TotalSeatStatus.AVAILABLE)
        );
        concertSeat = concertSeatJpaRepository.save(
                new ConcertSeat(1L, 10000, 1)
        );
    }

    @Test
    void 좌석_임시예약_비관적락_동시성_테스트() throws InterruptedException {
        // given
        int numberOfThreads = 100;
        ExecutorService service = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch latch = new CountDownLatch(numberOfThreads);

        for(int i = 0; i < numberOfThreads; i++) {
            service.execute(() -> {
                try{
                    // when
                    concertManager.reserveSeatWithLock(concertSchedule, userId, concertSeat.getId());
                }catch (Exception e) {
                    System.out.println(e.getMessage());
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();

        // then
        List<Reservation> reservations = reservationJpaRepository.findByConcertScheduleId(concertSchedule.getId());
        assertThat(reservations).hasSize(1);
    }

    @AfterEach
    void tearDown() {
        reservationJpaRepository.deleteAllInBatch();
        concertScheduleJpaRepository.deleteAll();
        concertSeatJpaRepository.deleteAll();
        concertJpaRepository.deleteAll();
    }

}
