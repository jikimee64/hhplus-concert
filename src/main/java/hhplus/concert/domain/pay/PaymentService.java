package hhplus.concert.domain.pay;

import hhplus.concert.domain.concert.ConcertRepository;
import hhplus.concert.domain.concert.ConcertSchedule;
import hhplus.concert.domain.concert.Reservation;
import hhplus.concert.domain.concert.ReservationStatus;
import hhplus.concert.domain.pay.dto.Receipt;
import hhplus.concert.domain.user.User;
import hhplus.concert.domain.user.UserRepository;
import hhplus.concert.domain.userqueue.UserQueueRepository;
import hhplus.concert.interfaces.api.support.ApiException;
import hhplus.concert.interfaces.api.support.error.ErrorCode;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.logging.LogLevel;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
@RequiredArgsConstructor
public class PaymentService {

    private final UserRepository userRepository;
    private final ConcertRepository concertRepository;
    private final UserQueueRepository userQueueRepository;
    private final PaymentRepository paymentRepository;

    public Receipt pay(String token, Long userId, Long concertScheduleId, Long seatId, LocalDate concertOpenDate) {
        User user = userRepository.findById(userId);

        Reservation selectedReservation = concertRepository.findReservation(concertScheduleId, seatId)
                .orElseThrow(() -> new ApiException(ErrorCode.E404, LogLevel.INFO, "Reservation not found concertScheduleId: " + concertScheduleId + ", seatId: " + seatId));
        checkAndProcessPayment(selectedReservation, user);

        userQueueRepository.deleteActiveToken(token);

        concertRepository.updateReservationStatus(ReservationStatus.RESERVED, concertScheduleId, seatId);

        ConcertSchedule concertSchedule = concertRepository.findConcertSchedule(concertScheduleId);
        updateTotalSeatStatus(concertSchedule);

        Payment savedPayment = savePayment(userId, selectedReservation);

        return new Receipt(
                savedPayment.getId(),
                concertSchedule.getConcert().getTitle(),
                concertSchedule.getOpenDate(),
                selectedReservation.getSeatPosition(),
                selectedReservation.getSeatAmount(),
                savedPayment.getCreatedAt()
        );
    }

    /**
     * 유저의 잔액과 예약한 좌석의 금액을 비교하여 충분한지 확인하고 차감
     * - 유저의 잔액이 충분하지 않을 경우 예외 발생
     */
    private void checkAndProcessPayment(Reservation reservation, User user) {
        boolean isSufficient = reservation.isUserAmountSufficient(user.getAmount());
        if (!isSufficient) {
            throw new ApiException(ErrorCode.E005, LogLevel.INFO, "seatAmount = " + reservation.getSeatAmount() + "userAmount = " + user.getAmount());
        }
        user.subtractAmount(reservation.getSeatAmount());
    }

    /**
     * 전체 좌석 마감되었을 경우 전체 좌석 마감 상태 업데이트
     */
    private void updateTotalSeatStatus(ConcertSchedule concertSchedule) {
        List<Reservation> reservations = concertRepository.findBy(concertSchedule.getId());
        long reserved = reservations.stream()
                .filter(reservation -> reservation.isReserved() || reservation.isTempReserved())
                .count();

        // TODO: 캐시 깨야 함
        if (concertSchedule.getTotalSeat() == reserved) {
            concertSchedule.updateTotalSeatStatusSoldOut();
            concertRepository.evictCachedConcertSchedule(concertSchedule.getConcert().getId());
        }
    }

    /*
     * 결제 데이터 삽입
     */
    private Payment savePayment(Long userId, Reservation selectedReservation) {
        return paymentRepository.save(new Payment(
                userId,
                selectedReservation.getId(),
                selectedReservation.getSeatAmount()
        ));
    }

}
